package concurrent.phase3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Demo29 {

    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test1() throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Callable<Integer>> callableList = Arrays.asList(
            () -> {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("the first finished");
            return 1;
        },
            () -> {
            TimeUnit.SECONDS.sleep(20);
            System.out.println("the second finished");
            return 2;
        });

        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(pool);
        List<Future<Integer>> futures = new ArrayList<>();
        callableList.forEach(e -> futures.add(completionService.submit(e)));
//        Future<Integer> future;
//        // take方法会阻塞住
//        while ((future = completionService.take()) != null) {
//            System.out.println(future.get());
//        }

        // 这里future可能为空，当然可以设置等待时间
        Future<Integer> future = completionService.poll(11, TimeUnit.SECONDS);
        System.out.println(future.get());
    }
}
