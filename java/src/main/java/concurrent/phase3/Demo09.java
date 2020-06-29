package concurrent.phase3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Demo09 {

    public static void main(String[] args) {
        final SemaphoreLock lock = new SemaphoreLock();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " running");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " get the lock");
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                System.out.println(Thread.currentThread().getName() + " release the lock");
            }).start();
        }
    }

    private static class SemaphoreLock {

        //只允许一个线程过去
        private final Semaphore semaphore = new Semaphore(2);

        private void lock() throws Exception {
            semaphore.acquire();
        }

        public void unlock() {
            semaphore.release();
        }
    }
}
