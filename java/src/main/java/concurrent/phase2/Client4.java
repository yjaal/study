package concurrent.phase2;

import java.util.Random;

public class Client4 extends Thread {

    private final ReqQueue queue;
    private final Random random;
    private final String sendValue;

    public Client4(ReqQueue queue, String sendValue) {
        this.queue = queue;
        this.sendValue = sendValue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Client-> request " + sendValue);
            queue.putRequest(new Request(sendValue));
            try {
                Thread.sleep(random.nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
