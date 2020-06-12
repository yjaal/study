package concurrent.phase2;

public class MyCountDown {

    private final int total;

    private int count;

    public MyCountDown(int total) {
        this.total = total;
    }

    public void countDown() {
        synchronized (this) {
            this.count++;
            this.notifyAll();
        }
    }

    public void await() throws InterruptedException {
        synchronized (this) {
            while (this.count != total) {
                this.wait();
            }
        }
    }
}
