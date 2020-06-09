package concurrent.phase2;

public abstract class Subject2 implements Runnable {

    //这里只是维护了一个观察者
    final protected Observer2 subject;

    public Subject2(final Observer2 subject) {
        this.subject = subject;
    }

    protected void notifyChange(final RunnableEvent event) {
        subject.onEvent(event);
    }

    public enum RunnableState {
        RUNNING, ERROR, DONE
    }

    public static class RunnableEvent {

        private final RunnableState state;
        private final Thread thread;
        private final Throwable cause;

        public RunnableEvent(RunnableState state, Thread thread, Throwable cause) {
            this.state = state;
            this.thread = thread;
            this.cause = cause;
        }

        public RunnableState getState() {
            return state;
        }

        public Thread getThread() {
            return thread;
        }

        public Throwable getCause() {
            return cause;
        }
    }
}
