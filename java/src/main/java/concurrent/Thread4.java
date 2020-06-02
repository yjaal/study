package concurrent;

import java.util.stream.IntStream;

public class Thread4 {

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 100)
                .forEach(i ->
                    System.out.println(Thread.currentThread().getName() + " -> " + i));
        });
        Thread t2 = new Thread(() -> {
            IntStream.range(1, 100)
                .forEach(i ->
                    System.out.println(Thread.currentThread().getName() + " -> " + i));
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        IntStream.range(1, 100)
            .forEach(i ->
                System.out.println(Thread.currentThread().getName() + " -> " + i));
    }
}
