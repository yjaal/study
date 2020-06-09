package concurrent.phase2;

import concurrent.phase2.Subject2.RunnableEvent;
import java.util.List;

public class ThreadLifeCycleObserver implements Observer2 {

    private final Object LOCK = new Object();

    /**
     * 这里启动几个线程，并监控其状态
     */
    public void concurrentQuery(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        ids.forEach(id -> new Thread(new Subject2(this) {
            @Override
            public void run() {
                try {
                    notifyChange(new RunnableEvent(RunnableState.RUNNING, Thread.currentThread(), null));
                    System.out.println("query for the id " + id);
                    notifyChange(new RunnableEvent(RunnableState.DONE, Thread.currentThread(), null));
                } catch (Exception e) {
                    notifyChange(new RunnableEvent(RunnableState.ERROR, Thread.currentThread(), e));
                }
            }
        }, id).start());
    }

    @Override
    public void onEvent(RunnableEvent event) {
        synchronized (LOCK) {
            System.out.println("The runnable [ " + event.getThread().getName()
                + " ] data change and state is " + event.getState());
            if (event.getCause() != null) {
                System.out.println("The runnable [ " + event.getThread().getName()
                    + " ] process failed");
                event.getCause().printStackTrace();
            }
        }
    }
}
