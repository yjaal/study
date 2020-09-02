package concurrent.phase3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Demo31 {

    public static void main(String[] args) throws InterruptedException {
        test();
        Thread.currentThread().join();
    }

    /**
     * 提交两个任务，接受其中一个任务的结果，但是另外一个线程还是会执行
     */
    private static void test() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("first task end");
            return "first";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("second task end");
            return "second";
        }), System.out::println);
    }

    private static void test1() {

    }
}
