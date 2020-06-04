package concurrent;

public class StaticLock {

    public synchronized static void m1() {
        try {
            System.out.println(Thread.currentThread().getName() + "抢到了锁(m1方法)");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void m2() {
        try {
            System.out.println(Thread.currentThread().getName() + "抢到了锁(m2方法)");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
