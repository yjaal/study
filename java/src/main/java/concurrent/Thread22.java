package concurrent;


import java.util.stream.IntStream;

public class Thread22 {

    public static void main(String[] args) throws InterruptedException {
        //测试拒绝策略
//        MyThreadPool pool = new MyThreadPool(6, 10);
        MyThreadPool pool = new MyThreadPool();
        IntStream.range(0, 40).forEach(i ->
            pool.submit(() -> {
                System.out.println("The runnable " + i + " begin serviced by " + Thread.currentThread());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable " + i + " end serviced by " + Thread.currentThread());
            }));

        Thread.sleep(500_000);
        pool.shutdown();
        pool.submit(() -> System.out.println("线程池被关闭后提交任务"));
    }
}
