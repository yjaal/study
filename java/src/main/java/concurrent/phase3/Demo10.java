package concurrent.phase3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo10 {

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread() + " start...");
                method1();
            }).start();
        }
    }

    public static void method1() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread() + " start running");
            Thread.sleep(5000);
            System.out.println(Thread.currentThread() + " end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
