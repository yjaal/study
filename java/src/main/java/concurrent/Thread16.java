package concurrent;

import concurrent.MyLock.TimeoutExceptin;
import java.util.stream.Stream;

public class Thread16 {

    public static void main(String[] args) {
        final MyLockImpl myLock = new MyLockImpl();
        Stream.of("线程1", "线程2", "线程3", "线程4").forEach(name ->
            new Thread(() -> {
                try {
                    myLock.lock(10);
                    System.out.println(Thread.currentThread().getName() + " have the lock");
                    work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutExceptin e) {
                    System.out.println(Thread.currentThread().getName() + "超时了");
                } finally {
                    myLock.unlock();
                }
            }, name).start());
    }

    private static void work() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is working");
        Thread.sleep(10_000);
    }
}
