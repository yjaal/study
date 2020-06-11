package concurrent.phase2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer extends Thread{

    private final ProducerData data;

    private int seq;

    private final static AtomicInteger count = new AtomicInteger(0);

    private final static Random random = new Random(1000);

    public Producer(ProducerData data, int seq) {
        super("P" + seq);
        this.data = data;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = Thread.currentThread().getName() + "->" + count.getAndIncrement();
                data.put(msg);
                System.out.println(Thread.currentThread().getName() + " -> Produce -> " + msg);
                Thread.sleep(random.nextInt(100));
            } catch (Exception e) {
                break;
            }
        }
    }
}
