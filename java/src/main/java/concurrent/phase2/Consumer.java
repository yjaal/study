package concurrent.phase2;

import java.util.Random;

public class Consumer extends Thread{

    private final ProducerData data;

    private int seq;

    private final static Random random = new Random(1000);

    public Consumer(ProducerData data, int seq) {
        super("C" + seq);
        this.data = data;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + " -> consume -> " + data.take());
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
