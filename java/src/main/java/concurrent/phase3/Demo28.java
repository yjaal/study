package concurrent.phase3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Demo28 {

    public static void main(String[] args) throws Exception {
//        testGet();
//        testTimeout();
//        testDone();
        testCancel();
    }

    private static void testGet() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<Integer> future = pool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("被中断后不会执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        // future 抛出了InterruptedException， 这里是main线程打断了，但是要注意，线程池还没有停止
        Thread.currentThread().interrupt();
        Integer res = future.get();
        System.out.println("结果： " + res);
        pool.shutdown();
    }

    private static void testTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<Integer> future = pool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                // 超时当前线程还是会执行，只是调用者不会执行了
                System.out.println("超时后还是会执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        // 这里设置超时时间，超时后线程池是没有停止的
        Integer res = future.get(5, TimeUnit.SECONDS);
        System.out.println("结果： " + res);
        pool.shutdown();
    }

    private static void testDone() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<Integer> future = pool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });
        System.out.println(future.get());
        // Completion may be due to normal termination, an exception, or cancellation
        System.out.println(future.isDone());
        pool.shutdown();
    }

    private static void testCancel() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<Integer> future = pool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("cancel后还是会执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });
        // 传入true会中断线程停止任务，传入false则会让线程正常执行至完成
        // 传入false参数只能取消还没有开始的任务，若任务已经开始了，就任由其运行下去。
        boolean canceled = future.cancel(true);
        System.out.println("取消成功：" + canceled);
        System.out.println("1. 是否被取消了：" + future.isCancelled());
        System.out.println("2. 是否完成了：" + future.isDone());
    }
}
