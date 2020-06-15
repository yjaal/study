package concurrent.phase1;

public class Thread5 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {

            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t1.interrupt();
            System.out.println("中断");
        });
        t2.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
