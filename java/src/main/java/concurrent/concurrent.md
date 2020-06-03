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













