package concurrent.phase3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo11 {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        ReentrantLockThread1 t1 = new ReentrantLockThread1(lock, condition);
        ReentrantLockThread2 t2 = new ReentrantLockThread2(lock, condition);

        new Thread(t1).start();
        new Thread(t2).start();
    }

    private static class ReentrantLockThread1 implements Runnable {

        private Lock lock;
        private Condition condition;

        public ReentrantLockThread1(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            try {
                this.lock.lock();
                System.out.println("RUN THREAD 1-1");
                this.condition.await();
                System.out.println("RUN THREAD 1-2");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.lock.unlock();
            }
        }
    }

    private static class ReentrantLockThread2 implements Runnable {

        private Lock lock;
        private Condition condition;

        public ReentrantLockThread2(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            try {
                this.lock.lock();
                System.out.println("RUN THREAD 2");
                this.condition.signal();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                this.lock.unlock();
            }
        }
    }
}
