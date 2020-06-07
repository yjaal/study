package concurrent.phase2;

public class Volatile1 {

    private volatile static int INIT_VAL = 0;

    private final static int MAX_LIMIT = 5;

    public static void main(String[] args) {
        new Thread(() -> {
            int localVal = INIT_VAL;
            while (localVal < MAX_LIMIT) {
                if (localVal != INIT_VAL) {
                    System.out.printf("reader: The value updated to [%d]\n", INIT_VAL);
                    localVal = INIT_VAL;
                }
            }
        }, "reader").start();

        new Thread(() -> {
            int localVal = INIT_VAL;
            while (INIT_VAL < MAX_LIMIT) {
                System.out.printf("writer: Updated the value  to [%d]\n", ++localVal);
                INIT_VAL = localVal;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "writer").start();

    }
}
