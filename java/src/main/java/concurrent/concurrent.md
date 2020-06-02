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

这里将线程设置为了守护线程，此时当`main`线程执行完时`t1`线程就立即结束了，此时不会管`t1`是否执行完了都会强制结束。

