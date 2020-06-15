package concurrent.phase1;

public class Thread11 {

    public static void main(String[] args) {
        DeadLock lock = new DeadLock();
        new Thread(() -> {
            lock.m1();
        }, "线程1").start();

        new Thread(() -> {
            lock.m2();
        }, "线程2").start();
    }

}
