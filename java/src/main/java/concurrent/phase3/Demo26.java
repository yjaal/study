package concurrent.phase3;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

public class Demo26 {

    public static void main(String[] args) {

        MyPool pool = new MyPool(1, 2, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            Thread::new, new AbortPolicy());

        pool.execute(new MyRunnable(1) {
            @Override
            public void run() {
                System.out.println("-----1----");
            }
        });
        pool.execute(new MyRunnable(2) {
            @Override
            public void run() {
                System.out.println("-----2----");
            }
        });
        pool.shutdown();
    }

    private abstract static class MyRunnable implements Runnable {

        private final int no;

        public MyRunnable(int no) {
            this.no = no;
        }

        protected int getData() {
            return this.no;
        }
    }

    private static class MyPool extends ThreadPoolExecutor {

        public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("init the " + ((MyRunnable) r).getData());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if (Objects.isNull(t)) {
                // 这里就表明线程还是存在的
                System.out.println("success");
            } else {
                System.out.println("failed");
            }
        }
    }
}
