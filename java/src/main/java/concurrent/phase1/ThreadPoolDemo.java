package concurrent.phase1;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 其中线程池使用
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class ThreadPoolDemo {

	public static void main(String[] args) throws InterruptedException {
		ThreadPoolDemo threadPoolDemo = new ThreadPoolDemo();
		threadPoolDemo.useScheduledThreadPool();
	}

	/**
	 * newCachedThreadPool 会根据任务来临的需要决定是否创建新的线程，也就是如果来了新任务又没有空闲线程，它就会新建一个线程
	 * 如果有空闲的，可能会重用
	 */
	public void useCachedThreadPool() throws InterruptedException {
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 1; i <= 10; i++) {
			final int count = i;
			pool.submit(() -> System.out.println("线程：" + Thread.currentThread() + "负责了" + count + "次任务"));
			//下面这行代码注释的话，线程池会新建10个线程，不注释的话，因为会复用老线程，不会产生10个线程
			Thread.sleep(1);
		}
		pool.shutdown();
	}

	/**
	 * 创建一个固定大小的、可重用是线程池
	 */
	public void useFixedThreadPool() throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(4);
		for (int i = 1; i <= 10; i++) {
			final int count = i;
			pool.submit(() -> System.out.println("线程：" + Thread.currentThread() + "负责了" + count + "次任务"));
			//下面这行代码注释的话，线程池会新建10个线程，不注释的话，因为会复用老线程，不会产生10个线程
			Thread.sleep(100);
		}
		pool.shutdown();
	}

	/**
	 * 创建一个定长线程池，支持定时及周期性任务执行
	 */
	public void useScheduledThreadPool() {
		// 指定大小为4
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);
		pool.scheduleAtFixedRate(() -> {
			Date now = new Date();
			System.out.println("线程" + Thread.currentThread() + "报时：" + now);
			// 延迟2s秒执行，每隔1s执行一次
		}, 2, 1, TimeUnit.SECONDS);
	}

	/**
	 * newWorkStealingPool创建一个带并行级别的线程池，并行级别决定了同一时刻最多有多少个线程在执行，
	 * 如不穿如并行级别参数，将默认为当前系统的CPU个数。下面用代码来体现这种并行的限制，从结果中可以看到，同一时刻只有两个线程执行
	 */
	public void useWorkStealingPool() {
		// 设置并行级别为2，即默认每时每刻只有2个线程同时执行
		ExecutorService pool = Executors.newWorkStealingPool(2);
		for (int i = 1; i <= 10; i++) {
			final int count = i;
			pool.submit(() -> {
				Date now = new Date();
				System.out.println("线程" + Thread.currentThread() + "完成任务：" + count + " 时间为：" + now.getSeconds());
				try {
					//此任务耗时1s
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		while (true) {
			//主线程陷入死循环，来观察结果，否则是看不到结果的
		}
	}
}
