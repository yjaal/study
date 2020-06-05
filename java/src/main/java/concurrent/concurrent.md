# 多线程

## 基础

### 1、`Thread`构造方法

```java
Thread(ThreadGroup group, Runnable target, String name, long stackSize)
```

这里注意最后一个参数

```java
package concurrent;
public class Thread1 {

    private static int count = 0;

    public static void main(String[] args) {
        try {
            add(0);
        } catch (StackOverflowError error) {
            error.printStackTrace();
            System.out.println(count);
        }
    }

    private static void add(int idx) {
        ++count;
        add(idx++);
    }
}
```

这种情况会抛出栈溢出的问题。

```java
package concurrent;

public class Thread2 {

    private static int count = 1;

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    add(1);
                } catch (StackOverflowError e) {
                    System.out.println(count);
                }
            }

            private void add(int i) {
                count++;
                add(i++);
            }
        });
        t1.start();
    }
}
```

此时运行时`count`最后的值时`14903`。继续改造下

```java
package concurrent;

public class Thread2 {

    private static int count = 1;

    public static void main(String[] args) {
        Thread t1 = new Thread(null, new Runnable() {
            @Override
            public void run() {
                try {
                    add(1);
                } catch (StackOverflowError e) {
                    System.out.println(count);
                }
            }
            private void add(int i) {
                count++;
                add(i++);
            }
        }, "thread1", 1 << 24);
        t1.start();
    }
}
```

此时会发现`count`的值变大了，这个参数就是用来控制栈（一个线程）大小的，让循环可以在达到这个大小之前不抛出栈溢出异常。**通过这个参数我们也可以明白，操作系统能支持的最大线程数也是有上限的，因为每个线程都需要消耗内存**。



最后说明一下，这个Runnable和Thread的这种设计其实就是策略设计模式的应用，所有线程都必须集成Runnable接口，实现run方法。



### 2、守护线程

```java
package concurrent;

public class Thread3 {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ing");
                Thread.sleep(100000);
                System.out.println(Thread.currentThread().getName() + " done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread done");
    }
}
```

可以使用`jConsole`进行监控（打开后启动程序会自动显示在其页面，双击选择即可），可以发现`main`线程执行完了，但是程序却没有立即退出，这是因为`JVM`发现还存在活跃线程，那么就会等待。

```java
package concurrent;

public class Thread3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ing");
                Thread.sleep(100000);
                System.out.println(Thread.currentThread().getName() + " done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.setDaemon(true);
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread done");
    }
}
```

这里将线程设置为了守护线程，此时当`main`线程执行完时`t1`线程就立即结束了，此时不会管`t1`是否执行完了都会强制结束。也就是说当启动一个线程`t1`，然后在`t1`中又创建了一个守护线程`t2`，那么当`t1`结束时，`t2`会立即结束。



### 3、Join

当前线程等待子线程执行完成之后再执行

```java
package concurrent;

import java.util.stream.IntStream;

public class Thread4 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 100)
                .forEach(i ->
                    System.out.println(Thread.currentThread().getName() + " -> " + i));
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IntStream.range(1, 100)
            .forEach(i ->
                System.out.println(Thread.currentThread().getName() + " -> " + i));
    }
}
```

如果不使用`join`，那么会交替打印，如果使用`join`，那么会等待`t1`执行完毕后主线程再执行。

```java
package concurrent;

import java.util.stream.IntStream;

public class Thread4 {

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 100)
                .forEach(i ->
                    System.out.println(Thread.currentThread().getName() + " -> " + i));
        });
        Thread t2 = new Thread(() -> {
            IntStream.range(1, 100)
                .forEach(i ->
                    System.out.println(Thread.currentThread().getName() + " -> " + i));
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        IntStream.range(1, 100)
            .forEach(i ->
                System.out.println(Thread.currentThread().getName() + " -> " + i));
    }
}
```

这里要注意：`join`针对的是主线程，上面程序在执行的时候`t1`和`t2`线程还是会交替执行，主线程会等待这两个线程执行完毕之后再执行。这是因为`t1`和`t2`都是主线程的子线程。当然`join`还可以设置等待时间。如果我们想让一个线程一直停在那里可以这样

```java
Thread.currentThread().join();
```

假设我们有三个线程去采集数据，此时我们需要统计总共消耗的时间，此时就可以使用`join`来完成。



### 4、中断

在`sleep、wait、join`时可以将线程打断，注意，这里的打断是打断**当前线程**

```java
package concurrent;

public class Thread5 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {

            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t1.interrupt();
            System.out.println("中断");
        });
        t2.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

此时我们发现并不会抛出`InterruptedException`异常，因为打断的是`t1`线程，而`join`的是主线程，如果将

```java
t1.interrupt(); --> main.interrupt();
```

此时才能捕获异常。

```java
package stopthreads;

/**
 * 描述：  catch了InterruptionException之后的优先选择：在方法签名中抛出异常。
 * 那么，在run()中就会强制try-catch。
 */
public class RightWayStopThreadInProduction implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadInProduction());
        thread.start();
        thread.sleep(1000);
        thread.interrupt();
    }

    @Override
    public void run() {
        while(true){
            System.out.println("go");
            throwInMethod();
        }
    }

    private void throwInMethod() {
        /**
         * 错误做法：这样做相当于就把异常给吞了，导致上层的调用无法感知到有异常
         * 正确做法应该是，抛出异常，而异常的真正处理，应该叫个调用它的那个函数。
         */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

这种情况下是无法将线程停止的，正确做法应该是`throwInMethod`抛出异常。

**`Java`没有提供任何机制来安全地终止线程。但它提供了中断(`Interruption`)，这是一种协作机制**，能够使一个线程终止另一个线程的当前工作。

这种协作式的方法是必要的，我们很少希望某个任务、线程或服务立即停止，**因为这种立即停止会使共享的数据结构处于不一致的状态**。上面这种通过中断抛异常的方式显然不是很好。



### 5、停止线程

停止线程可以使用上面那种方式，而`stop`方法建议不要使用。

中断方式停止线程

```java
package concurrent;

public class Thread7 {

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        worker.interrupt();
    }
    private static class Worker extends Thread {

        @Override
        public void run() {
            while (true) {
                //do something
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    //break;
                    return;
                }
            }
        }
    }
}
```



通过标志停止线程

```java
package concurrent;

public class Thread6 {

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        worker.shutdown();
    }

    private static class Worker extends Thread {

        private volatile boolean start = true;

        @Override
        public void run() {
            while (start) {

            }
        }

        public void shutdown() {
            start = false;
        }
    }
}
```

有时候可能在做一个耗时操作，然后阻塞在那里了。此时无法读取标志位，也无法监听到中断，此时如何强制停止线程呢？

```java
package concurrent;

public class ThreadService {

    private Thread executeThread;

    private boolean finished = false;

    public void execute(Runnable task) {
        executeThread = new Thread(() -> {
            Thread runner = new Thread(task);
            runner.setDaemon(true);
            runner.start();
            try {
                runner.join();
                finished = true;
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        });
        executeThread.start();
    }

    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if (System.currentTimeMillis() - currentTime >= mills) {
                //超时结束
                executeThread.interrupt();
                break;
            }
            try {
                //这里短暂休眠一下
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("执行线程将executeThread打断");
                break;
            }
        }
        finished = false;
    }
}
```



```java
package concurrent;

public class Thread8 {

    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();
        service.execute(() -> {
            while (true) {
                //do something
            }
        });
        //这里对上面的while任务设置一个超时时间
        service.shutdown(1000);
        long end = System.currentTimeMillis();
        System.out.println("超时停止：" + (end - start));
    }
}
```

这里可以看到我们由主线程调用起`ThreadService`，然后由`ThreadService`的子线程来执行具体的耗时任务。当耗时任务阻塞的时候我们将`ThreadService`打断，此时由于耗时任务是`ThreadService`的守护子线程，在`ThreadService`停止时，耗时任务必然停止。

### 6、同步

一个简单的对台取号场景

```java
package concurrent;

public class Thread9 {

    public static void main(String[] args) {
        final TicketWindowRunnable ticketWindow = new TicketWindowRunnable();
        Thread win1 = new Thread(ticketWindow, "一号窗口");
        Thread win2 = new Thread(ticketWindow, "二号窗口");
        Thread win3 = new Thread(ticketWindow, "三号窗口");
        win1.start();
        win2.start();
        win3.start();
    }
}
```



```java
package concurrent;

public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    private final static int MAX = 500;

    @Override
    public void run() {
        while (true) {
            if (index > MAX) {
                break;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "的号码是： " + (index++));
        }
    }
}
```

此时会出现最大号码超过500的情况。此时我们其实加一个锁即可解决

```java
package concurrent;

public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    private final static int MAX = 500;

    private final Object MONITOR = new Object();

    @Override
    public void run() {
        while (true) {
            synchronized (MONITOR) {
                if (index > MAX) {
                    break;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "的号码是： " + (index++));
            }
        }
    }
}
```

这里可以通过命令查看`class`文件的`dump`文件：

```
E:\software\jdk8u241\bin>javap -c  E:\project\study\java\target\classes\concurrent\TicketWindowRunnable.class
Compiled from "TicketWindowRunnable.java"
public class concurrent.TicketWindowRunnable implements java.lang.Runnable {
  public concurrent.TicketWindowRunnable();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: aload_0
       5: iconst_1
       6: putfield      #2                  // Field index:I
       9: aload_0
      10: new           #3                  // class java/lang/Object
      13: dup
      14: invokespecial #1                  // Method java/lang/Object."<init>":()V
      17: putfield      #4                  // Field MONITOR:Ljava/lang/Object;
      20: return

  public void run();
    Code:
       0: aload_0
       1: getfield      #4                  // Field MONITOR:Ljava/lang/Object;
       4: dup
       5: astore_1
       6: monitorenter
       7: aload_0
       8: getfield      #2                  // Field index:I
      11: sipush        500
      14: if_icmple     22
      17: aload_1
      18: monitorexit
      19: goto          93
      22: ldc2_w        #6                  // long 5l
      25: invokestatic  #8                  // Method java/lang/Thread.sleep:(J)V
      28: goto          36
      31: astore_2
      32: aload_2
      33: invokevirtual #10                 // Method java/lang/InterruptedException.printStackTrace:()V
      36: getstatic     #11                 // Field java/lang/System.out:Ljava/io/PrintStream;
      39: new           #12                 // class java/lang/StringBuilder
      42: dup
      43: invokespecial #13                 // Method java/lang/StringBuilder."<init>":()V
      46: invokestatic  #14                 // Method java/lang/Thread.currentThread:()Ljava/lang/Thread;
      49: invokevirtual #15                 // Method java/lang/Thread.getName:()Ljava/lang/String;
      52: invokevirtual #16                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      55: ldc           #17                 // String 的号码是：
      57: invokevirtual #16                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      60: aload_0
      61: dup
      62: getfield      #2                  // Field index:I
      65: dup_x1
      66: iconst_1
      67: iadd
      68: putfield      #2                  // Field index:I
      71: invokevirtual #18                 // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
      74: invokevirtual #19                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      77: invokevirtual #20                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      80: aload_1
      81: monitorexit
      82: goto          90
      85: astore_3
      86: aload_1
      87: monitorexit
      88: aload_3
      89: athrow
      90: goto          0
      93: return
    Exception table:
       from    to  target type
          22    28    31   Class java/lang/InterruptedException
           7    19    85   any
          22    82    85   any
          85    88    85   any
}
```

这里可以看到`monitorenter`和`monitorexit`分别代表锁获得与释放。

```java
package concurrent;

public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    private final static int MAX = 500;

    private final Object MONITOR = new Object();

    @Override
    public synchronized void run() {
        while (true) {
            if (index > MAX) {
                break;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "的号码是： " + (index++));
        }
    }
}
```

这里我们同步了方法，此时会发现其中一个线程将事都办完了，其他两个啥也没干。此时要明白此时的同步锁是**`this`**。这种情况下就是因为锁加的范围太大了，导致其他两个线程没事干，可以改小点

```java
package concurrent;

public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    private final static int MAX = 500;

    @Override
    public void run() {
        while (ticket()) {
        }
    }

    private synchronized boolean ticket() {
        if (index > MAX) {
            return false;
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "的号码是： " + (index++));
        return true;
    }
}
```

这里是和`synchronized(this)`效果是一样的。那如果加在静态代码块上时锁是谁呢？

```java
package concurrent;

public class Thread10 {


    public static void main(String[] args) {
        StaticLock lock = new StaticLock();
        new Thread(() -> {
            lock.m1();
        }, "线程1").start();

        new Thread(() -> {
            lock.m2();
        }, "线程2").start();
    }

}
```



```java
package concurrent;

public class StaticLock {

    public synchronized static void m1() {
        try {
            System.out.println(Thread.currentThread().getName() + "抢到了锁(m1方法)");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void m2() {
        try {
            System.out.println(Thread.currentThread().getName() + "抢到了锁(m2方法)");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

这里可以很明显看到线程2必须在线程1执行完后才能执行，这里的锁其实是`StaticLock.class`，这里的效果就和`synchronized(StaticLock.class)`一样了。下面看一个死锁的情况

```java
package concurrent;

public class Thread11 {

    public static void main(String[] args) {
        DeadLock lock = new DeadLock();
        new Thread(() ->{
            lock.m1();
        }, "线程1").start();

        new Thread(() ->{
            lock.m2();
        }, "线程2").start();
    }

}
```



```java
package concurrent;

public class DeadLock {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void m1() {
        synchronized (lock1) {
            System.out.println("当前线程是：" + Thread.currentThread().getName());
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                System.out.println("当前线程是：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void m2() {
        synchronized (lock2) {
            System.out.println("当前线程是：" + Thread.currentThread().getName());
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock1) {
                System.out.println("当前线程是：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
```

可以看到当两个线程都需要对方释放锁才能执行时就会发生死锁情况。此时会发现`CPU`和内存都无变化，程序也没有执行，此时可以通过相关命令检查

```
//查看进程号
E:\software\jdk8u241\bin>jps
17812 Launcher
10776 Jps
12168 Thread11
13180

//检查锁
E:\software\jdk8u241\bin>jstack 12168
......
Java stack information for the threads listed above:
===================================================
"线程2":
        at concurrent.DeadLock.m2(DeadLock.java:36)
        - waiting to lock <0x00000000d6964bc8> (a java.lang.Object)
        - locked <0x00000000d6964bd8> (a java.lang.Object)
        at concurrent.Thread11.lambda$main$1(Thread11.java:12)
        at concurrent.Thread11$$Lambda$2/1896277646.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)
"线程1":
        at concurrent.DeadLock.m1(DeadLock.java:17)
        - waiting to lock <0x00000000d6964bd8> (a java.lang.Object)
        - locked <0x00000000d6964bc8> (a java.lang.Object)
        at concurrent.Thread11.lambda$main$0(Thread11.java:8)
        at concurrent.Thread11$$Lambda$1/2121055098.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.
```

这里可以很明显看到有一个死锁。



### 7、线程间通信

线程间通信最常见的就是生产者消费者模式

```java
package concurrent;

public class Thread12 {

    public static void main(String[] args) {
        ProducerConsumer1 pc = new ProducerConsumer1();

        new Thread(() -> {
            while (true) {
                pc.producer();
            }
        }, "生产线程").start();

        new Thread(() -> {
            while (true) {
                pc.consumer();
            }
        }, "消费线程").start();
    }
}
```

这里使用一个标志标量进行通信

```java
package concurrent;

public class ProducerConsumer1 {

    private int i = 0;
    final private Object lock = new Object();
    private volatile boolean isProduced = false;

    public void producer() {
        synchronized (lock) {
            if (isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "->" + (i++));
                isProduced = true;
                lock.notify();
            }
        }
    }

    public void consumer() {
        synchronized (lock) {
            if (!isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "->" + (i));
                isProduced = false;
                lock.notify();
            }
        }
    }
}
```

但是这种写法在多线程的情况下就会挂住的情况

```java
package concurrent;

import java.util.stream.Stream;

public class Thread12 {

    public static void main(String[] args) {
        ProducerConsumer1 pc = new ProducerConsumer1();
        Stream.of("生产线程1", "生产线程2").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.producer();
                }
            }, name).start());

        Stream.of("消费线程1", "消费线程2").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.consumer();
                }
            }, name).start());

    }
}
```

这里其实是没有死锁的，主要是由于在调用`notify`的时候只唤醒了一个线程（不确定唤醒的是哪一个），导致最后出现四个线程全部进入到等待的情况。那把`notify`换成`notifyAll`行不行呢？这是不行的，着会造成多次生产或者多次消费。正确的做法如下，就是在条件不满足的时候一直等待

```java
package concurrent;

import java.util.stream.Stream;

public class Thread13 {

    public static void main(String[] args) {
        ProducerConsumer1 pc = new ProducerConsumer1();
        Stream.of("生产线程1", "生产线程2").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.producer();
                }
            }, name).start());

        Stream.of("消费线程1", "消费线程2").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.consumer();
                }
            }, name).start());
    }
}
```



```java
package concurrent;

public class ProducerConsumer2 {

    private int i = 0;
    final private Object lock = new Object();
    private volatile boolean isProduced = false;

    public void producer() {
        synchronized (lock) {
            while (isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "->" + (i++));
            isProduced = true;
            lock.notifyAll();
        }
    }

    public void consumer() {
        synchronized (lock) {
            while (!isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "->" + (i));
            isProduced = false;
            lock.notifyAll();
        }
    }
}
```



### 9、sleep 与 wait

* `sleep()` 是 `Thread` 类的静态本地方法；`wait()` 是`Object`类的成员本地方法
* `sleep() `方法可以在任何地方使用；`wait()` 方法则只能在同步方法或同步代码块中使用，否则抛出异常`Exception in thread "Thread-0" java.lang.IllegalMonitorStateException`
* `sleep()` 会休眠当前线程指定时间，释放 `CPU` 资源，不释放对象锁，休眠时间到自动苏醒继续执行；`wait() `方法放弃持有的对象锁，进入等待队列，当该对象被调用 `notify() / notifyAll()` 方法后才有机会竞争获取对象锁，进入运行状态
* `Thread.Sleep(0) `并非是真的要线程挂起`0`毫秒，意义在于这次调用`Thread.Sleep(0)`的当前线程确实的被冻结了一下，让其他线程有机会优先执行。`Thread.Sleep(0) `是你的线程暂时放弃`cpu`，也就是释放一些未用的时间片给其他线程或进程使用，就相当于一个让位动作。(当然这里是在单`CPU`情况下)



### 10、一个案例

有多个线程，然后每次需要控制最多五个线程同时执行，当五个中其中一个执行完后第六个线程才能加入进来运行。

```java
package concurrent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Thread14 {

    private final static LinkedList<Object> CONTROLS = new LinkedList<>();
    private final static int MAX_THREAD_COUNT = 5;

    public static void main(String[] args) {

        List<Thread> workers = new ArrayList<>();
        Stream.of("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
            .map(Thread14::createCaptureThread)
            .forEach(t -> {
                t.start();
                workers.add(t);
            });
        workers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("All of captures work finished");
    }

    private static Thread createCaptureThread(String name) {
        return new Thread(() -> {
            System.out.println("The worker [" + Thread.currentThread().getName() + "] begin capture data.");
            synchronized (CONTROLS) {
                while (CONTROLS.size() >= MAX_THREAD_COUNT) {
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONTROLS.addLast(new Object());
            }
            System.out.println("The worker [" + Thread.currentThread().getName() + "] is working.");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (CONTROLS) {
                System.out.println("The worker [" + Thread.currentThread().getName() + "] end capture data.");
                CONTROLS.removeFirst();
                CONTROLS.notifyAll();
            }
        }, name);
    }
}
```

这里通过一个集合来控制同时运行线程的数量，后面如果使用线程池可能会有更好的方式。这里同时也要注意：一个是这里当一个线程运行完后将其中一个线程移除，上面是直接移除最前面一个，这个可能会有问题，因为第一个可能还没运行完，可以单独创建一个线程类，其中维护一个是否执行完的标志字段，在移除的时候判断下这个标志位；其次这里一次性将所有线程都创建出来了，如果使用线程池，可以这样，将线程池作为锁（类似上面的`CONTROLS`），然后用一个`while`循环检测线程池中活跃的线程，当满足条件的时候像线程池中`submit`。下面使用线程池写了一个简单的例子

```java
package concurrent;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class Thread15 {

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(1024),
        new ThreadFactoryBuilder().setNameFormat("pool-%d").build(),
        new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            //这里是数据，可能是从数据库中查询出来的
            List<String> data = Collections.singletonList("线程" + i + "的数据");
            while (pool.getActiveCount() >= 5) {
                //wait
            }
            synchronized (pool) {
                if (pool.getActiveCount() < 5) {
                    pool.submit(() -> dealData(data));
                }
            }
        }
    }

    public static void dealData(List<String> data) {
        System.out.println(Thread.currentThread().getName() + " -> begin");
        data.forEach(System.out::println);
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " -> end");
    }
}
```

### 11、自定义锁

```java
package concurrent;

import java.util.stream.Stream;

public class Thread16 {

    public static void main(String[] args) {
        final MyLockImpl myLock = new MyLockImpl();
        Stream.of("线程1", "线程2", "线程3", "线程4").forEach(name ->
            new Thread(() -> {
                try {
                    myLock.lock();
                    System.out.println(Thread.currentThread().getName() + " have the lock");
                    work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    myLock.unlock();
                }
            }, name).start());
/*
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myLock.unlock();*/
    }

    private static void work() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is working");
        Thread.sleep(10_000);
    }
}
```



```java
package concurrent;

import java.util.Collection;

public interface MyLock {
    void lock() throws InterruptedException;
    void lock(long mills) throws InterruptedException, TimeoutExceptin;
    void unlock();
    Collection<Thread> getBlockedThread();
    int getBlockedThreadSize();
    class TimeoutExceptin extends Exception {

        public TimeoutExceptin(String msg) {
            super(msg);
        }
    }
}
```



```java
package concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MyLockImpl implements MyLock{

    private boolean initValue;

    private Collection<Thread> blockedThreads = new ArrayList<>();

    /**
     * initValue = true indicated the lock has been got by some thread
     */
    public MyLockImpl() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        //获取到锁之后发现现在被别人占用着，将自己进入等待状态
        while (initValue) {
            blockedThreads.add(Thread.currentThread());
            this.wait();
        }
        blockedThreads.remove(Thread.currentThread());
        this.initValue = true;
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutExceptin {

    }

    @Override
    public synchronized void unlock() {
        this.initValue = false;
        System.out.println(Thread.currentThread().getName() + " release the lock monitor");
        this.notifyAll();
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        //其他方法对此集合操作都是线程安全的，但是我们返回的时候不能让外界对其进行修改
        return Collections.unmodifiableCollection(blockedThreads);
    }

    @Override
    public int getBlockedThreadSize() {
        return blockedThreads.size();
    }
}
```

这里运行时没有问题的，但是这个锁有点简单了，不能防止别人乱用，这里如果将`Thread16`中注释代码释放开运行就会发现有问题了，这里也就是说其他线程可以将锁释放掉。这里在锁中定义一个变量记住获取到锁的线程，只有这个线程才能释放锁。

```java
package concurrent;
......

public class MyLockImpl implements MyLock {

    private boolean initValue;

    private Collection<Thread> blockedThreads = new ArrayList<>();

    private Thread currentThread;

    /**
     * initValue = true indicated the lock has been got by some thread
     */
    public MyLockImpl() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        //获取到锁之后发现现在被别人占用着，将自己进入等待状态
        while (initValue) {
            blockedThreads.add(Thread.currentThread());
            this.wait();
        }
        blockedThreads.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutExceptin {

    }

    @Override
    public synchronized void unlock() {
        if (Thread.currentThread() == currentThread) {
            this.initValue = false;
            System.out.println(Thread.currentThread().getName() + " release the lock monitor");
            this.notifyAll();
        }
    }
......
}
```



有时候当线程一直获取不到锁的时候我们希望线程不要继续等待了，这里我们看一个例子

```java
package concurrent;

public class Thread17 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> Thread17.run());
        t1.start();

        Thread t2 = new Thread(Thread17::run);
        t2.start();

        t1.interrupt();
        System.out.println(t1.isInterrupted());

        //休眠2s进行打断
        Thread.sleep(2_000);
        t2.interrupt();
        System.out.println(t2.isInterrupted());
    }


    private static synchronized void run() {
        System.out.println(Thread.currentThread().getName());
        while (true) {

        }
    }
}
```

这里会发现虽然我们将`t1`线程进行打断，`t2`线程还是没有获取到锁。同时程序也并没有停下来。这是因为这个方法通过修改了被调用线程的中断状态来告知那个线程, 说它被中断了. 对于非阻塞中的线程, 只是改变了中断状态, 即`Thread.isInterrupted()`将返回`true`; 对于可取消的阻塞状态中的线程, 比如等待在这些函数上的线程, `Thread.sleep(), Object.wait(), Thread.join()`, 这个线程收到中断信号后, 会抛出`InterruptedException`, 同时会把中断状态置回为true.但调用`Thread.interrupted()`会对中断状态进行复位。这里对`synchronized`进行打断说到底只是改了一个标志位，并不能将程序停下来。

下面对之前的自定义锁进行优化

```java
// MyLockImpl
@Override
public synchronized void lock(long mills) throws InterruptedException, TimeoutExceptin {
    if (mills <= 0) {
        lock();
    }
    long hasRemaining = mills;
    long endTime = System.currentTimeMillis() + mills;
    // 如果锁被别的线程拿到了
    while (initValue) {
        //这么长时间还没拿到锁，那就超时了
        if (hasRemaining <= 0) {
            throw new TimeoutExceptin("Time out");
        }
        blockedThreads.add(Thread.currentThread());
        this.wait(mills);
        hasRemaining = endTime - System.currentTimeMillis();
    }
    blockedThreads.remove(Thread.currentThread());
    this.initValue = true;
    this.currentThread = Thread.currentThread();
}
```

```java
package concurrent;

import concurrent.MyLock.TimeoutExceptin;
import java.util.stream.Stream;

public class Thread16 {

    public static void main(String[] args) {
        final MyLockImpl myLock = new MyLockImpl();
        Stream.of("线程1", "线程2", "线程3", "线程4").forEach(name ->
            new Thread(() -> {
                try {
                    myLock.lock(10);
                    System.out.println(Thread.currentThread().getName() + " have the lock");
                    work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutExceptin e) {
                    System.out.println(Thread.currentThread().getName() + "超时了");
                } finally {
                    myLock.unlock();
                }
            }, name).start());
    }

    private static void work() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is working");
        Thread.sleep(10_000);
    }
}
```



### 12、给应用设置一个钩子

```java
package concurrent;

/**
 * nohup java -cp . Thread18 &
 * 日志会输出到本地的nohup.out中
 */
public class Thread18 {
    public static void main(String[] args) {
        int i = 0;
        while (true) {
            try {
                Thread.sleep(1_000);
                System.out.println("working");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if (i > 20) {
                throw new RuntimeException("Error");
            }
        }
    }
}
```

这种情况就是只有发生异常的时候我们手动去检测才能知道应用已经挂掉了，现在我们想让程序在挂掉的时候主动通知。这里最好在`Linux`中试验。

```java
package concurrent;

/**
 * nohup java -cp . Thread18 &
 * 日志会输出到本地的nohup.out中
 */
public class Thread18 {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The app will be exit.");
            notifyAndRelease();
        }));
        int i = 0;
        while (true) {
            try {
                Thread.sleep(1_000);
                System.out.println("working");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if (i > 20) {
                throw new RuntimeException("Error");
            }
        }
    }

    private static void notifyAndRelease() {
        System.out.println("通知或唤醒别的线程...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("释放资源...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("notifyAndRelease over");
    }
}
```

这里第一个试验是运行程序，我们在程序中添加了计数，计数`20`次就抛异常，此时可以看到程序会执行`notifyAndRelease`方法；第二个是将计数注释掉，我们直接使用`kill [进程号]`杀掉进程，此时我们可以看到还是可以调用`notifyAndRelease`方法。但是如果使用`kill -9 [进程号]`则会直接将程序杀掉，不会执行钩子线程。这里使用了`Runtime`，`Runtime` 类代表着`Java`程序的运行时环境，每个`Java`程序都有一个`Runtime`实例，该类会被自动创建，我们可以通过`Runtime.getRuntime()` 方法来获取当前程序的`Runtime`实例。

参考：`https://blog.csdn.net/sinat_19171485/article/details/47913869`



### 13、捕获线程异常和调用栈

由于线程的`run`方法是不能抛出异常的，当线程中出现异常的时候，有些异常我们可以直接捕获，但是这里捕获对于外界是不清楚的，而对于运行时异常我们是无法直接捕获的，同时主线程也是无法捕获的。

```java
package concurrent;

public class Thread19 {

    private final static int A = 10;
    private final static int B = 0;

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(100);
                int result = A / B;
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程1");
        t.start();
    }
}
```

这里设置了一个异常，发生异常后主线程是无法获取到异常的，要知道异常只能通过日志。

```java
package concurrent;

public class Thread19 {

    private final static int A = 10;
    private final static int B = 0;

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(100);
                int result = A / B;
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程1");

        t.setUncaughtExceptionHandler((thread, e) -> {
            System.out.println(e);
            System.out.println(thread);
        });
        t.start();
    }
}
```

同时有时候我们想在日志中打印相关的调用栈信息

```java
package concurrent;

public class Thread20 {

    public static void main(String[] args) {
        new Demo1().m1();
    }
}
----------
package concurrent;
public class Demo1 {

    public void m1(){
        new Demo2().m2();
    }
}
----------
package concurrent;

import java.util.Arrays;
import java.util.Optional;

public class Demo2 {
    public void m2() {
        Arrays.asList(Thread.currentThread().getStackTrace()).stream()
            .filter(e -> !e.isNativeMethod())
            .forEach(e -> Optional.of(e.getClassName() + ":" + e.getMethodName() + ":" + e.getLineNumber())
                .ifPresent(System.out::println));
    }
} 
```



### 14、ThreadGroup

```java
package concurrent;

public class Thread21 {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getThreadGroup());

        ThreadGroup tg1 = new ThreadGroup("TG1");
        Thread t1 = new Thread(tg1, "t1"){
            @Override
            public void run() {
                System.out.println(getThreadGroup().getName());
                System.out.println(getThreadGroup().getParent().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        ThreadGroup tg2 = new ThreadGroup(tg1, "TG2");
        System.out.println(tg2.getName());
        System.out.println(tg2.getParent().getName());
    }
}
```



### 15、线程池




