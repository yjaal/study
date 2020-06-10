package concurrent.phase2;

public class Server4 extends Thread {

    private final ReqQueue queue;
    private volatile boolean flag = true;

    public Server4(ReqQueue queue) {
        this.queue = queue;
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
                System.out.println("Received the empty request.");
                continue;
            }
            System.out.println("Server-> " + request.getValue());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
