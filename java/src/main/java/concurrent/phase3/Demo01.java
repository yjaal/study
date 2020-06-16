package concurrent.phase3;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Demo01 {

    public static void main(String[] args) {
        final AtomicIntegerFieldUpdater<TestMe> updater =
            AtomicIntegerFieldUpdater.newUpdater(TestMe.class, "i");
        final TestMe me = new TestMe();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 20; j++) {
                    int v = updater.getAndIncrement(me);
                    System.out.println(Thread.currentThread().getName() + "->" + v);
                }
            }).start();
        }
    }

    private static class TestMe{
        volatile int i ;
    }
}

