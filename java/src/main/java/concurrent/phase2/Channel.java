package concurrent.phase2;

import java.util.Arrays;

/**
 * 相当于一个传送带，货物全部放在此传送带上，货物会往传送带上放，工人会从传送带上取下来进行处理
 */
public class Channel {

    private final static int MAX_REQUEST = 100;

    /**
     * 要处理的货物或事情数组
     */
    private final ChannelRequest[] requestQueue;

    private int head;

    private int tail;

    /**
     * 工人数量
     */
    private int count;

    /**
     * 工人数组
     */
    private final WorkThread[] workPool;

    public Channel(int workers) {
        this.requestQueue = new ChannelRequest[MAX_REQUEST];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.workPool = new WorkThread[workers];
        this.init();
    }

    private void init() {
        for (int i = 0; i < workPool.length; i++) {
            workPool[i] = new WorkThread("Worker-" + i, this);
        }
    }

    /**
     * 此方法类似于开始干活
     */
    public void startWorker() {
        Arrays.asList(workPool).forEach(WorkThread::start);
    }

    /**
     * 将货物往传送带上放
     */
    public synchronized void put(ChannelRequest request) {
        while (count >= workPool.length) {
            try {
                this.wait();
            } catch (Exception e) {

            }
        }
        this.requestQueue[tail] = request;
        this.tail = (tail + 1) % requestQueue.length;
        this.count++;
        this.notifyAll();
    }

    /**
     * 从传送带上取货物
     */
    public synchronized ChannelRequest take() {
        while (count <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ChannelRequest request = this.requestQueue[head];
        //随机一下
        this.head = (this.head + 1) % requestQueue.length;
        this.count--;
        this.notifyAll();
        return request;
    }
}
