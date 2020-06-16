package concurrent.phase3;


import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest1 {

    private final AtomicInteger value = new AtomicInteger(0);

    private static final AtomicIntegerTest1 lock = new AtomicIntegerTest1();

    private Thread lockedThread;

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    doSomething();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void doSomething() throws Exception {
        try {
            lock.tryLock();
            System.out.println(Thread.currentThread().getName() + " get the lock");
            Thread.sleep(100_000);
        } finally {
            lock.unlock();
        }
    }

    public void tryLock() throws Exception {
        //如果是0，表示锁为释放状态，否则为关闭状态
        boolean success = value.compareAndSet(0, 1);
        if (!success) {
            throw new RuntimeException("Get the lock failed");
        } else {
            lockedThread = Thread.currentThread();
        }
    }

    public void unlock() {
        if (0 == value.get()) {
            return;
        }
        if (lockedThread == Thread.currentThread()) {
            value.compareAndSet(1, 0);
        }
    }
}
