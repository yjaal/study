摘自：`https://liuzhengyang.github.io/2017/05/11/cas/`

# CAS简介

CAS，在Java并发应用中通常指CompareAndSwap或CompareAndSet，即比较并交换。CAS是一个原子操作，它比较一个内存位置的值并且只有相等时修改这个内存位置的值为新的值，保证了新的值总是基于最新的信息计算的，如果有其他线程在这期间修改了这个值则CAS失败。CAS返回是否成功或者内存位置原来的值用于判断是否CAS成功。下面会分析CAS的作用、和锁的区别、以及实现方式。代码以Hotspot JDK1.8为基础进行分析。



# 和lock的区别

使用lock的方式(synchronized关键字或Lock类等)是一种悲观锁机制。但是大多数情况下并没有大量的竞争，相对而言CAS是一种乐观策略，认为竞争很少出现，当竞争发生时抛给调用方处理重试还是其他处理方式, 由于没有加锁带来的较高开销和加锁中的临界区限制，这种无锁(lock-free)机制相比加锁具有更高的扩展性。

使用lock同样可以完成同样的工作，例如可以通过锁来实现一个CAS操作。

```
public class SimulateCAS {
    private int value;

    public synchronized int getValue() {
        return value;
    }

    public boolean compareAndSet(int expect, int newValue) {
        synchronized (this) {
            if (value == expect) {
                value = newValue;
                return true;
            }
        }
        return false;
    }
}
```



在java.util.concurrent.atomics.AtomicInteger中就使用和提供了很多CAS方法。
如下面的方法，比较当前AtomicInteger的值，如果等于expect则并交换成update, 整体是一个原子方法，如果当前值是1并且有其他线程同时在执行compareAndSet(1, 2)，则只有一个能够执行成功。

```
public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}
```



# CAS常见使用场景

常见的使用场景是替换锁进行数据修改，常见的使用方法是获取当前值，根据当前值计算出要设置的值，然后使用CAS尝试交换，如果成功返回，如果失败可以选择重试或者采取其他策略。

例如使用上面的SimulateCAS实现一个原子的i++

```
    public class CasCounter {
    private SimulateCAS simulateCAS;

    public CasCounter() {
        this.simulateCAS = new SimulateCAS();
    }

    public int getCount() {
        return simulateCAS.getValue();
    }

    public int incrementAndGet() {
        int value;
        int newValue;
        do {
            value = simulateCAS.getValue();
            newValue = value + 1;
        } while(!simulateCAS.compareAndSet(value, newValue));
        return newValue;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CasCounter casCounter = new CasCounter();
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                casCounter.incrementAndGet();
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(casCounter.getCount());
    }
}
```



# Java中原子对象的实现和JVM的CAS实现

那么在java.util.concurrent.atomic保重的原子变量是如何实现原子操作的呢。
以`AtomicInteger`为例，可以看到它内部使用的`sun.misc.Unsafe`类来完成CAS操作，Unsafe类是更接近底层的操作类，可以在内存级别获取操作对象内存中的数据。
value使用volatile修饰，保证修改对其他线程的可见性。

```
public class AtomicInteger {
     private static final Unsafe unsafe = Unsafe.getUnsafe();
     private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile int value;

    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    public final int incrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
    }
}
```



其中getAndAddInt类似前面我们实现的原子增操作

```
public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
}
```



看下openjdk中compareAndSwapInt的实现

```
// unsafe.cpp
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
  UnsafeWrapper("Unsafe_CompareAndSwapInt");
  oop p = JNIHandles::resolve(obj);
  jint* addr = (jint *) index_oop_from_field_offset_long(p, offset);
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;
UNSAFE_END
```



// atomic.cpp中

```
jbyte Atomic::cmpxchg(jbyte exchange_value, volatile jbyte* dest, jbyte compare_value) {
  assert(sizeof(jbyte) == 1, "assumption.");
  uintptr_t dest_addr = (uintptr_t)dest;
  uintptr_t offset = dest_addr % sizeof(jint);
  volatile jint* dest_int = (volatile jint*)(dest_addr - offset);
  jint cur = *dest_int;
  jbyte* cur_as_bytes = (jbyte*)(&cur);
  jint new_val = cur;
  jbyte* new_val_as_bytes = (jbyte*)(&new_val);
  new_val_as_bytes[offset] = exchange_value;
  while (cur_as_bytes[offset] == compare_value) {
    jint res = cmpxchg(new_val, dest_int, cur);
    if (res == cur) break;
    cur = res;
    new_val = cur;
    new_val_as_bytes[offset] = exchange_value;
  }
  return cur_as_bytes[offset];
}
```



以linux x86为例，MP表示multiprocessor，即多处理器。最终根据具体的处理器架构转换成汇编指令来实现CAS。
当多处理器时需要在前面加上lock指令。
这里的`cmpxchgl`是x86和Intel架构中的compare and exchange指令。
在实际执行时，CPU可以通过锁总线或锁缓存

```
inline jint     Atomic::cmpxchg    (jint     exchange_value, volatile jint*     dest, jint     compare_value) {
  int mp = os::is_MP();
  __asm__ volatile (LOCK_IF_MP(%4) "cmpxchgl %1,(%3)"
                    : "=a" (exchange_value)
                    : "r" (exchange_value), "a" (compare_value), "r" (dest), "r" (mp)
                    : "cc", "memory");
  return exchange_value;
}
```



# CAS存在的问题

CAS可能存在ABA问题，就是一个值从A变成了B又变成了A，使用CAS操作不能发现这个值发生变化了，处理方式是可以使用携带类似时间戳的版本`AtomicStampedReference`