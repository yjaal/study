package concurrent.phase1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 线程池要素： 1、任务队列 2、拒绝策略（异常、丢弃、阻塞、临时队列） 3、初始化值（init, active, max）
 */
public class MyThreadPool extends Thread {

    //初始活跃线程数量
    private int size;

    //队列的大小
    private final int queueSize;

    //拒绝策略
    private final MyDiscardPolicy discardPolicy;

    /**
     * 线程池状态，如果为false表示没有被销毁，为true表示被销毁了
     */
    private volatile boolean destroy = false;

    private static final int DEFAULT_SIZE = 10;

    private static volatile int seq;

    private static final String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";
    private static final ThreadGroup GROUP = new ThreadGroup("POOL_GROUP");

    /**
     * 等待队列
     */
    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    /**
     * 工作线程集合
     */
    private static final List<WorkTask> CURRENT_THREADS = new ArrayList<>();

    //默认队列最大的大小
    private static final int DEFAULT_TASK_QUEUE_SIZE = 2000;

    /**
     * 下面几个参数的含义就是最初线程池创建min个线程，当后面发现线程明显不够用当时候增加创建线程， 直到数量达到active，后面发现还是不够，则继续增加到max个，当然反之亦然
     */
    private int min;

    private int max;

    private int active;

    public static final MyDiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("提交线程数量超出限制，直接丢弃");
    };

    public MyThreadPool() {
        this(DEFAULT_SIZE, DEFAULT_TASK_QUEUE_SIZE);
    }

    public MyThreadPool(int size, int queueSize) {
        this(size, queueSize, DEFAULT_DISCARD_POLICY);
    }

    public MyThreadPool(int size, int queueSize, MyDiscardPolicy discardPolicy) {
        this.size = size;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    private void init() {
        this.min = 4;
        this.active = 8;
        this.max = 12;
        for (int i = 0; i < min; i++) {
            creatWorkTasks();
        }
        this.size = this.min;
        this.start();
    }

    @Override
    public void run() {
        while (!destroy) {
            System.out.printf("Pool#Min:%d, Active:%d, Max:%d, Current:%d, QueueSize:%d\n",
                min, active, max, size, TASK_QUEUE.size());
            synchronized (CURRENT_THREADS) {
                try {
                    Thread.sleep(5_000);
                    if (TASK_QUEUE.size() > active && size < active) {
                        for (int i = size; i < active; i++) {
                            creatWorkTasks();
                        }
                        System.out.println("线程池被扩充了到了 active 个数");
                        this.size = active;
                    } else if (TASK_QUEUE.size() > active && size < max) {
                        for (int i = active; i < max; i++) {
                            creatWorkTasks();
                        }
                        System.out.println("线程池被扩充了到了 max 个数");
                        this.size = max;
                    } else if (TASK_QUEUE.isEmpty() && size > active) {
                        int releaseSize = size - active;
                        for (Iterator<WorkTask> it = CURRENT_THREADS.iterator(); it.hasNext(); ) {
                            if (releaseSize <= 0) {
                                break;
                            }
                            WorkTask task = it.next();
                            //可能当前线程还未执行完
                            while (task.taskState != TaskState.FREE) {
                                Thread.sleep(1000);
                            }
                            task.close();
                            task.interrupt();
                            it.remove();
                            releaseSize--;
                        }
                        size = active;
                        System.out.println("线程缩减到了 active 个数");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void creatWorkTasks() {
        WorkTask workTask = new WorkTask(GROUP, THREAD_PREFIX + (seq++));
        workTask.start();
        CURRENT_THREADS.add(workTask);
    }

    /**
     * 对外提供接口用于提交线程
     */
    public void submit(Runnable runnable) {
        if (this.destroy) {
            throw new IllegalStateException("线程池已被销毁关闭，不能提交线程");
        }
        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() >= queueSize) {
                try {
                    discardPolicy.discard();
                } catch (DiscardException e) {
                    System.out.println("此线程被拒绝时异常" + e);
                }
            }
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    public void shutdown() throws InterruptedException {
        //如果队列不是空的，则表明有线程还在执行，需要让其执行完毕
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(100);
        }
        synchronized (CURRENT_THREADS) {
            int initVal = CURRENT_THREADS.size();
            while (initVal > 0) {
                for (WorkTask task : CURRENT_THREADS) {
                    if (task.getTaskState() == TaskState.BLOCKED) {
                        task.interrupt();
                        task.close();
                        initVal--;
                    } else {
                        Thread.sleep(50);
                    }
                }
            }
        }
        this.destroy = true;
        System.out.println("线程池被关闭");
    }

    public static class DiscardException extends Exception {

        public DiscardException(String msg) {
            super(msg);
        }
    }

    public int getSize() {
        return size;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public boolean isDestroy() {
        return this.destroy;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getActive() {
        return active;
    }

    public interface MyDiscardPolicy {

        void discard() throws DiscardException;
    }

    private static class WorkTask extends Thread {

        private volatile TaskState taskState = TaskState.FREE;

        public WorkTask(ThreadGroup group, String name) {
            super(group, name);
        }

        public TaskState getTaskState() {
            return this.taskState;
        }

        public void close() {
            this.taskState = TaskState.DEAD;
        }

        @Override
        public void run() {
            OUTER:
            //线程要是FREE状态时才能去队列中取任务来执行
            while (this.taskState == TaskState.FREE) {
                Runnable t;
                //某个线程要提交进来是先放在队列，然后再从队列中去取
                synchronized (TASK_QUEUE) {
                    //如果队列是空则等待
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            break OUTER;
                        }
                    }
                    //队列不为空，则拿出来第一个，然后需要立即释放锁，不然后面的线程无法提交
                    t = TASK_QUEUE.removeFirst();
                }
                if (t != null) {
                    taskState = TaskState.RUNNING;
                    t.run();
                    taskState = TaskState.FREE;
                }
            }
        }
    }

    enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }
}
