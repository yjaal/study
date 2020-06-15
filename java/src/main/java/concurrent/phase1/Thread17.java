package concurrent.phase1;

public class Thread17 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> Thread17.run());
        t1.start();

        Thread t2 = new Thread(Thread17::run);
        t2.start();

        t1.interrupt();
        System.out.println(t1.isInterrupted());

        //休眠2s进行打断
        Thread.sleep(2_000);
        t2.interrupt();
        System.out.println(t2.isInterrupted());
    }


    private static synchronized void run() {
        System.out.println(Thread.currentThread().getName());
        while (true) {

        }
    }

}
