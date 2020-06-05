package concurrent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 1、任务队列 2、拒绝策略（异常、丢弃、阻塞、临时队列） 3、初始化值（init, active, max）
 */
public class Thread22 {

    private final int size;

    private static final int DEFAULT_SIZE = 10;

    private static volatile int seq;

    private static final String THREAD_PREFIX = "SIMPLE_THREAD_POOL_";
    private static final ThreadGroup GROUP = new ThreadGroup("SIMPLE_THREAD_POOL");

    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private final static List<WorkTask> THREAD_QUEUE = new ArrayList<>();

    public Thread22(int size) {
        this.size = size;
        init();
    }

    private void init() {

    }

    private void creatWorkTasks(String name) {
        WorkTask workTask = new WorkTask(GROUP, THREAD_PREFIX + (seq++));
        workTask.start();
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
            while (this.taskState != TaskState.DEAD) {
                Thread t;
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
                    //队列不为空，则拿出来第一个
//                    t = TASK_QUEUE.removeFirst();
                    if (t != null) {
                        taskState = TaskState.RUNNING;
                        t.run();
                        taskState = TaskState.FREE;
                    }
                }
            }
        }
    }

    static enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }


}
