package concurrent.phase2;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Client7 {

    private static final Random random = new Random(1000);

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(5);
        System.out.println("第一阶段任务多线程处理");
        IntStream.rangeClosed(1, 5).forEach(i -> {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " is working");
                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }, String.valueOf(i)).start();
        });
        try {
            //注意这里不是wait，而是await
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("等待第一阶段多线程全部执行完成，第二阶段任务处理");
        System.out.println("finish");
    }
}
