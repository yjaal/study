package concurrent.phase3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo23 {

    public static void main(String[] args) throws InterruptedException {
        test4();
    }

    public static void test4() throws InterruptedException {
        // return new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
        //             ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
        ExecutorService pool = Executors.newWorkStealingPool();
        List<Callable<String>> callableList = IntStream.range(0, 20).boxed().map(i -> (Callable<String>) () -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(100);
            return "task->" + i;
        }).collect(Collectors.toList());

        // 利用 Java 8 的流来处理 invokeAll 调用返回的所有 Future
        // 批量提交可调用的另一种方法是 invokeAny()，它与 invokeAll() 略有不同。 该方法不会返回所有的 Future 对象，它只返回第一个执行完毕任务的结果。
        pool.invokeAll(callableList).stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);

        pool.shutdown();
    }

    public static void test3() {
        // return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
        //                                    new LinkedBlockingQueue<Runnable>()));
        // 其实这里和 Executors.newFixedThreadPool(1); 差不多，这里主要是为了隐藏一些API，如getActiveCount
        ExecutorService pool = Executors.newSingleThreadExecutor();
    }

    public static void test2() throws InterruptedException {
        // return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        // 这里就不会自动销毁了，同时核心线程数量和最大线程数量相等，最大活跃线程数量就为10
        ExecutorService pool = Executors.newFixedThreadPool(10);
        System.out.println(((ThreadPoolExecutor) pool).getActiveCount());
        pool.execute(() -> System.out.println("========="));
        System.out.println(((ThreadPoolExecutor) pool).getActiveCount());
        IntStream.range(0, 20).boxed().forEach(i -> pool.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("* " + Thread.currentThread().getName() + " ->" + i + " done");
        }));
        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor) pool).getActiveCount());
        pool.shutdown();
    }

    public static void test1() throws InterruptedException {
        // return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        // 这里队列只能放一个线程，此时就会一直创建，直到最大线程，60s后会将所有线程回收释放掉，线程池自动销毁了
        ExecutorService pool = Executors.newCachedThreadPool();
        System.out.println(((ThreadPoolExecutor) pool).getActiveCount());
        pool.execute(() -> System.out.println("========="));
        System.out.println(((ThreadPoolExecutor) pool).getActiveCount());

        IntStream.range(0, 100).boxed().forEach(i -> pool.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("* " + Thread.currentThread().getName() + " ->" + i + " done");
        }));
        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor) pool).getActiveCount());
    }
}
