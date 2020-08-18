package concurrent.phase3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

public class Demo21 {

    public static void main(String[] args) {
        ThreadPoolExecutor pool = test1();
        int activeAccount = -1;
        int queueSize = -1;
        while (true) {
            if (activeAccount != pool.getActiveCount() || queueSize != pool.getQueue().size()) {
                System.out.println("活跃线程数: " + pool.getActiveCount());
                System.out.println("核心线程数: " + pool.getCorePoolSize());
                System.out.println("队列线程数: " + pool.getQueue().size());
                System.out.println("最大线程数: " + pool.getMaximumPoolSize());
                activeAccount = pool.getActiveCount();
                queueSize = pool.getQueue().size();
                System.out.println("-------------------");
            }
        }
    }

    public static ThreadPoolExecutor test1() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(1),
            r -> {
                Thread t = new Thread(r);
                return t;
            }, new AbortPolicy());
        System.out.println("the thread pool created done");
        // 通过提交不同数量线程来检测
        pool.execute(() -> sleepSeconds(100));
        pool.execute(() -> sleepSeconds(10));
        pool.execute(() -> sleepSeconds(100));
        return pool;
    }

    private static void sleepSeconds(long sec) {
        try {
            System.out.println("* " + Thread.currentThread().getName() + " *");
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
