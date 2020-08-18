package concurrent.phase3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Demo22 {

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            r -> {
                Thread t = new Thread(r);
                return t;
            }, new AbortPolicy());

        IntStream.range(0, 20).boxed().forEach(i -> pool.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("* " + Thread.currentThread().getName() + " ->" + i + " done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        // 表示将线程池停止，但是是非阻塞的，需要当前工作的所有线程执行完，而队列中的线程不会提交，直接退出，无返回值
        // 而shutdownNow则会将正在工作都线程处理完，同样不会再执行queue中的线程，而是将队列中的线程返回出去，也是非阻塞的
        pool.shutdown();

        // 如果想在这里阻塞住
        try {
            // 设置一个最大等待时间，若线程都执行完了则不会继续等待
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
