package concurrent.phase3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

public class Demo14 {

    private final static StampedLock lock = new StampedLock();
    private final static List<Long> data = new ArrayList<>();

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        Runnable readTask = () -> {
            for (; ; ) {
                read();
            }
        };

        Runnable writeTask = () -> {
            for (; ; ) {
                write();
            }
        };

        for (int i = 0; i < 8; i++) {
            service.submit(readTask);
        }
        for (int i = 0; i < 1; i++) {
            service.submit(writeTask);
        }
    }

    private static void write() {
        long stamped = -1;
        try {
            stamped = lock.writeLock();
            data.add(System.currentTimeMillis());
            System.out.println("W-");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(stamped);
        }
    }

    private static void read() {
        long stamped = lock.tryOptimisticRead();
        // 若校验不通过，表明有写锁，则去获取悲观读锁
        if (!lock.validate(stamped)) {
            try {
                stamped = lock.readLock();
                Optional.of(
                    data.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))
                ).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(stamped);
            }
        }
    }
}
