package concurrent.phase3;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Demo20 {

    private static final Random random = new Random(System.currentTimeMillis());

    static class Athlete extends Thread{

        private final int no;
        private final Phaser phaser;

        public Athlete(int no, Phaser phaser) {
            this.no = no;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                System.out.println(no + ": start first work");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end first work");
                // 此方法并不会阻塞，同时也不会对计数器进行变更
                phaser.arrive();

                System.out.println(no + ": start second work");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end second work");

                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 一次性注册4个线程
        Phaser phaser = new Phaser(5);
        for (int i = 0; i < 4; i++) {
            new Athlete(i, phaser).start();
        }
        phaser.arriveAndAwaitAdvance();
        System.out.println("the first phase work done");
    }
}
