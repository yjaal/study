package concurrent.phase2;

import java.util.Random;

/**
 * 相当于工人
 */
public class WorkThread extends Thread {

    private static final Random random = new Random(10000);

    private final Channel channel;

    public WorkThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            channel.take().execute();
            try {
                //每个工人做事效率是不同的
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
