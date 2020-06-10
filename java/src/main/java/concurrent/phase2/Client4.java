package concurrent.phase2;

public class Client4 extends Thread {

    private final ReqQueue queue;
    private final String sendValue;

    public Client4(ReqQueue queue, String sendValue) {
        this.queue = queue;
        this.sendValue = sendValue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Client-> request " + sendValue);
            queue.putRequest(new Request(sendValue));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ReqQueue queue = new ReqQueue();
        new Client4(queue, "Alex").start();
        Server4 server = new Server4(queue);
        server.start();
        server.join();
        server.close();
    }
}
