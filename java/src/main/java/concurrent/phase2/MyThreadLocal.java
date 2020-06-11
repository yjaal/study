package concurrent.phase2;

import java.util.HashMap;
import java.util.Map;

public class MyThreadLocal<T> {

    private final Map<Thread, T> storage = new HashMap<>();

    public void set(T t) {
        synchronized (this) {
            storage.put(Thread.currentThread(), t);
        }
    }

    public T get() {
        synchronized (this) {
            T value = storage.get(Thread.currentThread());
            if (null == value) {
                return initValue();
            }
            return value;
        }
    }

    private T initValue() {
        return null;
    }

    private final static MyThreadLocal<String> threadLocal = new MyThreadLocal();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            threadLocal.set("Thread-T1: ");
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            threadLocal.set("Thread-T2: ");
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("------------");
        System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
    }
}
