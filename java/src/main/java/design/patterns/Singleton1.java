package design.patterns;

/**
 * 单例模式：饿汉式
 * 线程安全，调用效率高，但是，不能延时加载
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class Singleton1 {
	/**
	 * 提供静态属性，这里就不管后面会不会用到，初始化时就加载一个实例
	 */
	private static Singleton1 instance = new Singleton1();

	/**
	 * 构造器私有化
	 */
	private Singleton1() {
	}

	/**
	 * 这里不需要使用同步，因为加载类的时候（static修饰）本身就是线程安全的了
	 * 没有对什么资源进行非原子性操作，同时这里没有同步，当然调用效率就高了
	 */
	public static /*synchronized*/ Singleton1 getInstance() {
		return instance;
	}
}
