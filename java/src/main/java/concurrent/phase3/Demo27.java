package concurrent.phase3;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo27 {

    public static void main(String[] args) throws Exception {
//        testInvokeAny();
        testTimeout();
    }

    private static void testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
            System.out.println(Thread.currentThread().getName() + " --> " + i);
            return i;
        }).collect(Collectors.toList());

        // 这是一个同步方法，这里任意执行一个线程
        Integer val = pool.invokeAny(callableList);
        System.out.println("finished, value: " + val);
        //其他线程是否还会执行呢？
        // 从打印结果中可以发现其他线程不会再执行，已经被取消了
        pool.shutdown();
    }

    private static void testTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        // 这是一个同步方法，这里任意执行一个线程
        // 这里测试下超时
        Integer val = pool.invokeAny(
            IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                System.out.println(Thread.currentThread().getName() + " --> " + i);
                return i;
            }).collect(Collectors.toList()), 3, TimeUnit.SECONDS
        );
        System.out.println("finished, value: " + val);
        pool.shutdown();
    }

    private static void testInvokeAll() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
            System.out.println(Thread.currentThread().getName() + " --> " + i);
            return i;
        }).collect(Collectors.toList());

        // 这里可以看到执行所有，但是有一个问题是这里虽然使用了并行流，但是仍然要分成三个阶段，这个问题在jdk9中已解决
        pool.invokeAll(callableList).parallelStream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(System.out::println);
    }
}
