package concurrent.phase1;

import java.util.Collection;

public interface MyLock {

    void lock() throws InterruptedException;

    void lock(long mills) throws InterruptedException, TimeoutExceptin;

    void unlock();

    Collection<Thread> getBlockedThread();

    int getBlockedThreadSize();


    class TimeoutExceptin extends Exception {

        public TimeoutExceptin(String msg) {
            super(msg);
        }
    }
}
