package concurrent.phase3;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;

public class Demo03 {

    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(1000);
//        Counter counter = new StupidCounter();
        Counter counter = new CasCounter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            service.submit(new CounterRunnable(counter, 10000));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println("Counter result:" + counter.getCounter());
        System.out.println("cost: " + (end - start));
    }

    interface Counter {

        void increment();

        long getCounter();
    }

    private static class CasCounter implements Counter {

        private volatile long counter = 0;

        private Unsafe unsafe;

        private static Unsafe getUnsafe() {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                return (Unsafe) f.get(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private long offset;

        public CasCounter() throws Exception {
            this.unsafe = getUnsafe();
            this.offset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
        }

        @Override
        public void increment() {
            long current = counter;
            while (!unsafe.compareAndSwapLong(this, offset, current, current + 1)) {
                current = counter;
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    private static class StupidCounter implements Counter {

        private long counter = 0;

        @Override
        public void increment() {
            counter++;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    private static class CounterRunnable implements Runnable {

        private final Counter counter;
        private final int num;

        public CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                counter.increment();
            }
        }
    }
}
