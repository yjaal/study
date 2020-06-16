package concurrent.phase3;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Demo02 {

    private volatile int i ;
    private AtomicIntegerFieldUpdater<Demo02> updater =
        AtomicIntegerFieldUpdater.newUpdater(Demo02.class, "i");

    public void update(int newValue) {
        updater.compareAndSet(this, i, newValue);
    }

    public int get() {
        return i;
    }

    public static void main(String[] args) {
        Demo02 demo02 = new Demo02();
        demo02.update(10);
        System.out.println(demo02.get());
    }
}
