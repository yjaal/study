package concurrent.phase2;

/**
 * 一个票据的具体实现
 */
public class AsynFuture<T> implements MyFuture<T> {

    private volatile boolean done = false;

    private T result;

    /**
     * 用于让外部调用通知结果
     */
    public void done(T result) {
        synchronized (this) {
            this.result = result;
            this.done = true;
            this.notifyAll();
        }
    }

    /**
     * 用于获取结果
     */
    @Override
    public T get() throws InterruptedException {
        synchronized (this) {
            while (!done) {
                this.wait();
            }
        }
        return result;
    }
}
