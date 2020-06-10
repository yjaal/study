package concurrent.phase2;

import java.util.Random;

public class Server4 extends Thread {

    private final ReqQueue queue;
    private final Random random;
    private volatile boolean flag = true;

    public Server4(ReqQueue queue) {
        this.queue = queue;
        random = new Random(System.currentTimeMillis());
    }

    public void close() {
        this.flag = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while (flag) {
            Request request = queue.getRequest();
            if (null == request) {

            }
            System.out.println("Server-> " + request.getValue());
            try {
                Thread.sleep(random.nextInt());
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
