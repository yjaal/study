package concurrent.phase2;

public class SchedulerThread extends Thread {

    private final ActivationQueue queue;

    public SchedulerThread(ActivationQueue queue) {
        this.queue = queue;
    }

    public void invoke(MethodRequest request) {
        this.queue.put(request);
    }

    @Override
    public void run() {
        while (true) {
            queue.take().execute();
        }
    }
}
