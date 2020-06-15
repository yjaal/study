package concurrent.phase1;

public class Thread10 {


    public static void main(String[] args) {
        StaticLock lock = new StaticLock();
        new Thread(() -> {
            lock.m1();
        }, "线程1").start();

        new Thread(() -> {
            lock.m2();
        }, "线程2").start();
    }

}
