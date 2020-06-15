package concurrent.phase1;

public class Thread2 {

    private static int count = 1;

    public static void main(String[] args) {
        Thread t1 = new Thread(null, new Runnable() {
            @Override
            public void run() {
                try {
                    add(1);
                } catch (StackOverflowError e) {
                    System.out.println(count);
                }
            }

            private void add(int i) {
                count++;
                add(i++);
            }
        }, "thread1", 1 << 24);
        t1.start();
    }
}
