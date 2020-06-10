package concurrent.phase2;

public class ReadWriteLock {

    //当前正在读的线程数量
    private int readingReaders = 0;

    //当前等待读的线程数量
    private int waitingReaders = 0;

    //当前正在写的线程数量（>=0）
    private int writingWriters = 0;

    //当前等待写的线程数量
    private int waitingWriters = 0;

    //是否更偏向与写
    private boolean preferWriter = true;

    public ReadWriteLock() {
        this(true);
    }

    public ReadWriteLock(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    public synchronized void readLock() throws InterruptedException {
        //刚获取到锁的时候肯定是一个等待状态
        this.waitingReaders++;
        try {
            //如果此时有正在写的线程，则等待
            while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
                this.wait();
            }
            this.readingReaders++;
        } finally {
            //被中断或者被唤醒执行则等待读线程数量减少
            this.waitingReaders--;
        }
    }

    public synchronized void readUnlock() {
        this.readingReaders--;
        this.notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        this.waitingWriters++;
        try {
            //有读线程或者有写线程时都不能进行写
            while (this.readingReaders > 0 || this.writingWriters > 0) {
                this.wait();
            }
            this.writingWriters++;
        } finally {
            this.waitingWriters--;
        }
    }

    public synchronized void writeUnlock() {
        this.writingWriters--;
        this.notifyAll();
    }
}
