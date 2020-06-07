package concurrent.phase2;

public class Volatile2 {

    private static int INIT_VAL = 0;

    private final static int MAX_LIMIT = 50;

    public static void main(String[] args) {
        new Thread(() -> {
            while (INIT_VAL < MAX_LIMIT) {
                System.out.println("T1 -> " + (++INIT_VAL));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER-1").start();

        new Thread(() -> {
            while (INIT_VAL < MAX_LIMIT) {
                System.out.println("T2 -> " + (++INIT_VAL));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER-2").start();

    }
}
