package concurrent.phase2;

import java.util.Arrays;

public class SharedData {

    private final char[] buffer;

    private final ReadWriteLock lock = new ReadWriteLock();

    public SharedData(int size) {
        this.buffer = new char[size];
        Arrays.fill(buffer, '*');
    }

    public char[] read() throws InterruptedException {
        try {
            lock.readLock();
            return doRead();
        } finally {
            lock.readUnlock();
        }
    }

    private char[] doRead() {
        //返回一个副本出去
        char[] newBuff = new char[buffer.length];
        System.arraycopy(buffer, 0, newBuff, 0, buffer.length);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        return newBuff;
    }

    public void write(char c) throws InterruptedException {
        try {
            lock.writeLock();
            doWrite(c);
        } finally {
            lock.writeUnlock();
        }
    }

    private void doWrite(char c) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = c;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }
}
