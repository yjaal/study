package concurrent.phase3;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Demo17 {

    private static final Random random = new Random(System.currentTimeMillis());

    static class Task extends Thread{
        private final Phaser phaser;

        public Task(Phaser phaser) {
            this.phaser = phaser;
            //task进来之后先要注册，而不是一开始就固定好有几个线程
            this.phaser.register();
            start();
        }

        @Override
        public void run() {
            System.out.println("The worker [" + getName() + "] is working...");
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                // 这里相当于CountDownLatch的await方法
                this.phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser();
        IntStream.rangeClosed(1, 5).boxed().map(e -> phaser).forEach(Task::new);
        // 这里将main线程也注册进来，相比起来这里可以动态的添加，而CountDownLatch和CyclicBarrier则需要一开始就确定
        phaser.register();
        phaser.arriveAndAwaitAdvance();
        System.out.println("task done");
    }
}
