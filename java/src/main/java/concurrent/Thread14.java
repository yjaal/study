package concurrent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Thread14 {

    private final static LinkedList<Object> CONTROLS = new LinkedList<>();
    private final static int MAX_THREAD_COUNT = 5;


    public static void main(String[] args) {

        List<Thread> workers = new ArrayList<>();
        Stream.of("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
            .map(Thread14::createCaptureThread)
            .forEach(t -> {
                t.start();
                workers.add(t);
            });
        workers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("All of captures work finished");
    }

    private static Thread createCaptureThread(String name) {
        return new Thread(() -> {
            System.out.println("The worker [" + Thread.currentThread().getName() + "] begin capture data.");
            synchronized (CONTROLS) {
                while (CONTROLS.size() >= MAX_THREAD_COUNT) {
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONTROLS.addLast(new Object());
            }
            System.out.println("The worker [" + Thread.currentThread().getName() + "] is working.");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (CONTROLS) {
                System.out.println("The worker [" + Thread.currentThread().getName() + "] end capture data.");
                CONTROLS.removeFirst();
                CONTROLS.notifyAll();
            }
        }, name);
    }
}

