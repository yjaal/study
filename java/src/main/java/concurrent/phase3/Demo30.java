package concurrent.phase3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Demo30 {

    public static void main(String[] args) throws Exception {
//        test();
//        test1();
//        test2();
//        test3();

        // 将BiFunction同时作用于两个阶段的结果
        // 下面这里的s1就是前面执行的结果，而s2就是输出"java"的CompletableFuture的结果
        CompletableFuture.supplyAsync(() -> "Hello ")
            .thenApply(s -> s + "world ")
            .thenApply(String::toUpperCase)
            .thenCombine(CompletableFuture.completedFuture("java "), (s1, s2) -> s1 + s2)
            .thenAccept(System.out::println);

        System.out.println("----------");

        // BiConsumer支持同时对两个Stage的结果进行操作
        String original = "Message ";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase)
            .thenAcceptBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                (s1, s2) -> result.append(s1).append(s2));
        System.out.println(result);
    }

    private static void test() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        // runAsync不关心前面的结果
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("execute");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, pool).whenComplete((v, t) -> System.out.println("done"));

        // CompletableFuture中有默认的线程池，而其中的线程都是守护线程，如果没有休眠，那么main线程执行完后线程池中的所有线程都会关闭
        // 当然如果我们想使用自己的线程池也可以，只需要将自定义线程池作为runAsync的第二个参数即可
        TimeUnit.SECONDS.sleep(30);
    }

    /**
     * 这里可以分别设置任务执行成功或者失败时的回调处理任务，无需等待
     */
    private static void test1() throws Exception {
        // 这里任务是一个Supplier对象
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(Demo30::fetchPrice);
        //执行成功，回调任务是一个Consumer对象，对应的还有thenRunAsync
        future.thenAccept(result -> System.out.println("success, result: " + result));
        // 执行失败，回调任务是一个Function对象
        future.exceptionally(e -> {
            System.out.println("failed");
            e.printStackTrace();
            return null;
        });
        // 这里要注意：如果不休眠，可能任务还未执行完主线程就关闭了，此时CompletableFuture默认使用的线程池会立刻关闭
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 多个Completable串行执行, 第一个CompletableFuture根据证券名称查询证券代码，第二个CompletableFuture根据证券代码查询证券价格
     */
    private static void test2() throws Exception {
        // 第一个任务:Supplier对象
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> queryCode("中国石油"));

        // future1成功后继续执行下一个任务:Function对象
        CompletableFuture<Double> future2 = future1.thenApplyAsync(Demo30::fetchPrice);

        // 执行成功后打印结果
        future2.thenAccept(result -> System.out.println("result: " + result));

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 同时从新浪和网易查询证券代码，只要任意一个返回结果，就进行下一步查询价格，查询价格也同时从新浪和网易查询，只要任意一个返回结果，就完成操作
     */
    private static void test3() throws Exception {
        // 第一阶段
        CompletableFuture<String> future1FromSina = CompletableFuture.supplyAsync(
            () -> queryCode("中国石油", "https://finance.sina.com.cn/code/"));
        CompletableFuture<String> future1From163 = CompletableFuture.supplyAsync(
            () -> queryCode("中国石油", "https://money.163.com/code/"));

        // 用anyOf合并为一个新的CompletableFuture:
        // 当然对应的还有allOf
        // 这里有一个问题就是当其中一个执行完成会进入到下一阶段，但是未执行完成的线程还是会执行
        CompletableFuture<Object> future1Any = CompletableFuture.anyOf(future1FromSina, future1From163);

        // 第二阶段
        CompletableFuture<Double> future2FromSina = future1Any.thenApplyAsync(
            code -> fetchPrice((String) code, "https://finance.sina.com.cn/price/"));
        CompletableFuture<Double> future2From163 = future1Any.thenApplyAsync(
            code -> fetchPrice((String) code, "https://money.163.com/price/"));

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> future2Any = CompletableFuture.anyOf(future2FromSina, future2From163);

        // 最终结果:
        future2Any.thenAccept(result -> System.out.println("result: " + result));

        TimeUnit.SECONDS.sleep(30);
    }

    private static String queryCode(String name, String url) {
        System.out.println("query code from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    private static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }

    private static String queryCode(String name) {
        System.out.println("name: " + name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    private static Double fetchPrice() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }

    private static Double fetchPrice(String code) {
        System.out.println("code: " + code);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }
}
