package concurrent.phase3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo25 {

    public static void main(String[] args) {
        test2();
    }

    private void test1() {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.execute(() -> {
            System.out.println("执行相关线程任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();

        // 调用shutdown后肯定状态时关闭的，但是并不代表一定就终止了
        System.out.println("线程池是否关闭: " + pool.isShutdown());
        System.out.println("线程池是否暂停: " + pool.isTerminated());
        System.out.println("线程池是否停止过程中: " + ((ThreadPoolExecutor) pool).isTerminating());
        // 不能再执行
//        pool.execute(() -> System.out.println("是否还可以执行?"));
    }

    private static void test2() {
        // 传入自己的工厂方法来捕获异常
        ExecutorService pool = Executors.newFixedThreadPool(1, new MyThreadFactory());
        pool.execute(() -> {
            System.out.println("执行相关线程任务");
            try {
                Thread.sleep(1000);
                int i = 1 / 0;
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        pool.shutdown();
    }


    private static class MyThreadFactory implements ThreadFactory {

        private static final AtomicInteger SEQ = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("ThreadName:" + SEQ.getAndIncrement());
            // 这里通过实现一个handle方法来捕获异常
            thread.setUncaughtExceptionHandler((t, cause) -> {
                System.out.println("the thread" + thread.getName() + " execute failed");
                cause.printStackTrace();
            });
            return thread;
        }
    }
}
