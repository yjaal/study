package concurrent.phase2;

import java.util.Random;

/**
 * 有几个人往传送带上放东西
 */
public class TransportThread extends Thread {

    private final Channel channel;

    private static final Random random = new Random(10000);

    public TransportThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                ChannelRequest request = new ChannelRequest(getName(), i);
                this.channel.put(request);
                Thread.sleep(random.nextInt(1000));
            }
        } catch (Exception e) {

        }
    }
}
