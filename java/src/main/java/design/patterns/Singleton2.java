package design.patterns;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例模式：懒汉式
 * 线程安全，效率不高，但是可以延时加载
 * 有些资源可能很大，我们一开始可能并不需要，如果使用饿汉式，那会浪费资源
 * 用的时候才会加载，资源利用率提高了，但每次调用都需要同步，调用效率就低了
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class Singleton2 {
	/**
	 * 类加载的时候没有初始化，这样就有可能发生线程安全问题
	 * 使用volatile避免指令重排序
	 */
	private static volatile Singleton2 instance;

	private Singleton2() {
	}

	/**
	 * 会出现线程安全性问题
	 */
	public static Singleton2 getInstance1() {
		if (instance == null) {
			try {
				//睡眠来增大线程安全性问题
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			instance = new Singleton2();
		}
		return instance;
	}

	/**
	 * 在方法上加同步会导致性能很低
	 */
	public static /*synchronized*/ Singleton2 getInstance2() {
		if (instance == null) {
			//使用同步代码块，但是还是不能解决问题，因为可能同时又两个
			//线程进入到了if语句内，于是就创建了两个对象
			synchronized (Singleton2.class) {
				instance = new Singleton2();
			}
		}
		return instance;
	}

	/**
	 * 双重检查加锁
	 */
	public static Singleton2 getInstance3() {
		if (instance == null) {
			synchronized (Singleton2.class) {
				if (instance == null) {
					/*
					 * 下面这一步在cpu中执行的顺序为
					 * 1、申请一块内存
					 * 2、实例化一个对象
					 * 3、将instance指向这块内存
					 * 但是由于指令重排序，实际顺序可能是312，此时instance就不为空了
					 * 那另一个线程在使用的时候就会出问题，此时可以使用volatile修饰instance
					 */
					instance = new Singleton2();
				}
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			threadPool.execute(() -> System.out.println(Thread.currentThread() + ":" + Singleton2.getInstance1()));
		}
		threadPool.shutdown();
	}
}
