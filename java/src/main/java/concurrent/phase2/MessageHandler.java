package concurrent.phase2;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {

    private final static Random random = new Random(1000);

    private final static Executor executor = Executors.newFixedThreadPool(5);

    public void request(String msg) {
        executor.execute(() -> {
            try {
                Thread.sleep(random.nextInt(100));
                System.out.println("The message[ " + msg + " ] will be handle by "
                    + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        ((ExecutorService)executor).shutdown();
    }
}
