package concurrent.phase3;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

    private static Set<Integer> set = new HashSet<>();

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger value = new AtomicInteger(0);
        Thread t1 = new Thread(() -> {
            int x = 0;
            while (x < 500) {
                int v = value.getAndIncrement();
                set.add(v);
                System.out.println(Thread.currentThread().getName() + ":" + v);
                x++;
            }
        });

        Thread t2 = new Thread(() -> {
            int x = 0;
            while (x < 500) {
                int v = value.getAndIncrement();
                set.add(v);
                System.out.println(Thread.currentThread().getName() + ":" + v);
                x++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(set.size());
    }
}
