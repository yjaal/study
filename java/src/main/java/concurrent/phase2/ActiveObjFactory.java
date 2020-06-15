package concurrent.phase2;

public final class ActiveObjFactory {

    private ActiveObjFactory() {

    }

    public static ActiveObj createActiveObj() {
        Servant servant = new Servant();
        ActivationQueue queue = new ActivationQueue();
        SchedulerThread schedulerThread = new SchedulerThread(queue);
        ActiveObjProxy proxy = new ActiveObjProxy(schedulerThread, servant);
        schedulerThread.start();
        return proxy;
    }
}
