package concurrent.phase1;

public class ProducerConsumer1 {

    private int i = 0;
    final private Object lock = new Object();
    private volatile boolean isProduced = false;

    public void producer() {
        synchronized (lock) {
            if (isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "->" + (i++));
                isProduced = true;
                lock.notify();
            }
        }
    }

    public void consumer() {
        synchronized (lock) {
            if (!isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "->" + (i));
                isProduced = false;
                lock.notify();
            }
        }
    }
}
