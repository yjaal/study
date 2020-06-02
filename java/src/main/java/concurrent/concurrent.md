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

此时会发现`count`的值变大了，这个参数就是用来控制栈（一个线程）大小的，让循环可以在达到这个大小之前不抛出栈溢出异常。



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

如果不使用`join`，那么会交替打印，如果使用`join`，那么会等待t1执行完毕后主线程再执行。

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

这种协作式的方法是必要的，我们很少希望某个任务、线程或服务立即停止，**因为这种立即停止会使共享的数据结构处于不一致的状态**。



### 5、停止线程





















