package concurrent;

public class ProducerConsumer2 {

    private int i = 0;
    final private Object lock = new Object();
    private volatile boolean isProduced = false;

    public void producer() {
        synchronized (lock) {
            while (isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "->" + (i++));
            isProduced = true;
            lock.notifyAll();
        }
    }

    public void consumer() {
        synchronized (lock) {
            while (!isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "->" + (i));
            isProduced = false;
            lock.notifyAll();
        }
    }
}
