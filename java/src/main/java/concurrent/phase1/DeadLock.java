package concurrent.phase1;

public class DeadLock {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void m1() {
        synchronized (lock1) {
            System.out.println("当前线程是：" + Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                System.out.println("当前线程是：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void m2() {
        synchronized (lock2) {
            System.out.println("当前线程是：" + Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock1) {
                System.out.println("当前线程是：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
