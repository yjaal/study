package concurrent.phase2;

public class Client10 {

    public static void main(String[] args) {
        AppServer server = new AppServer();
        server.start();
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.shutdown();
    }
}
