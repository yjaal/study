package concurrent.phase2;

import java.util.Random;

public class Client2 {

    public static void main(String[] args) {
        final SharedData data = new SharedData(10);
        new ReaderWork(data).start();
        new ReaderWork(data).start();
        new ReaderWork(data).start();
        new ReaderWork(data).start();
        new ReaderWork(data).start();

        new WriterWork(data, "fasdf").start();
        new WriterWork(data, "fasddsfaf").start();
    }


    public static class WriterWork extends Thread {

        private static final Random random = new Random(System.currentTimeMillis());
        private final SharedData data;
        private final String filler;

        private int index = 0;

        @Override
        public void run() {
            try {
                while (true) {
                    char c = nextChar();
                    data.write(c);
                    System.out.println(Thread.currentThread().getName() + " writes "+ c);
                    Thread.sleep(random.nextInt(1000));

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private char nextChar() {
            char c = filler.charAt(index);
            index++;
            if (index >= filler.length()) {
                index = 0;
            }
            return c;
        }

        public WriterWork(SharedData data, String filler) {
            this.data = data;
            this.filler = filler;
        }
    }

    public static class ReaderWork extends Thread {

        private static final Random random = new Random(System.currentTimeMillis());
        private final SharedData data;

        @Override
        public void run() {
            try {
                while (true) {
                    char[] readBuff = data.read();
                    System.out.println(Thread.currentThread().getName() + " reads "
                        + String.valueOf(readBuff));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public ReaderWork(SharedData data) {
            this.data = data;
        }
    }
}
