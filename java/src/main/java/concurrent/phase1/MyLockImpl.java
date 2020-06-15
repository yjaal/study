package concurrent.phase1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MyLockImpl implements MyLock {

    private boolean initValue;

    private Collection<Thread> blockedThreads = new ArrayList<>();

    private Thread currentThread;

    /**
     * initValue = true indicated the lock has been got by some thread
     */
    public MyLockImpl() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        //获取到锁之后发现现在被别人占用着，将自己进入等待状态
        while (initValue) {
            blockedThreads.add(Thread.currentThread());
            this.wait();
        }
        blockedThreads.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeoutExceptin {
        if (mills <= 0) {
            lock();
        }
        long hasRemaining = mills;
        long endTime = System.currentTimeMillis() + mills;
        // 如果锁被别的线程拿到了
        while (initValue) {
            //这么长时间还没拿到锁，那就超时了
            if (hasRemaining <= 0) {
                throw new TimeoutExceptin("Time out");
            }
            blockedThreads.add(Thread.currentThread());
            this.wait(mills);
            hasRemaining = endTime - System.currentTimeMillis();
        }
        blockedThreads.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void unlock() {
        if (Thread.currentThread() == currentThread) {
            this.initValue = false;
            System.out.println(Thread.currentThread().getName() + " release the lock monitor");
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        //其他方法对此集合操作都是线程安全的，但是我们返回的时候不能让外界对其进行修改
        return Collections.unmodifiableCollection(blockedThreads);
    }

    @Override
    public int getBlockedThreadSize() {
        return blockedThreads.size();
    }
}
