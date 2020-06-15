package concurrent.phase1;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class Thread15 {

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(1024),
        new ThreadFactoryBuilder().setNameFormat("pool-%d").build(),
        new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            //这里是数据，可能是从数据库中查询出来的
            List<String> data = Collections.singletonList("线程" + i + "的数据");
            while (pool.getActiveCount() >= 5) {
                //wait
            }
            synchronized (pool) {
                if (pool.getActiveCount() < 5) {
                    pool.submit(() -> dealData(data));
                }
            }
        }
    }

    public static void dealData(List<String> data) {
        System.out.println(Thread.currentThread().getName() + " -> begin");
        data.forEach(System.out::println);
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " -> end");
    }
}

