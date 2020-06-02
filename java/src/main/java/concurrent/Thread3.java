package concurrent;

public class Thread3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ing");
                Thread.sleep(100000);
                System.out.println(Thread.currentThread().getName() + " done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.setDaemon(true);
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread done");
    }
}
