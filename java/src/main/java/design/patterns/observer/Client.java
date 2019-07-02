package design.patterns.observer;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class Client {

	public static void main(String[] args) {
		ObserverA o1 = new ObserverA();
		ObserverA o2 = new ObserverA();
		ObserverA o3 = new ObserverA();

		ConcreteSubject subject = new ConcreteSubject();
		//将观察者注册到主题
		subject.register(o1);
		subject.register(o2);
		subject.register(o3);

		//主题内容变化
		subject.setState(300);

		System.out.println(o1.getMyState());
		System.out.println(o2.getMyState());
		System.out.println(o3.getMyState());
	}

	/**
	 * 使用java自带工具类实现
	 */
	public void test() {
		//创建主题
		ConcreteSubject1 subject = new ConcreteSubject1();

		//创建观察者
		ObserverB o1 = new ObserverB();
		ObserverB o2 = new ObserverB();
		ObserverB o3 = new ObserverB();
		ObserverB o4 = new ObserverB();

		//将观察者对象添加到目标对象subject的容器中
		subject.addObserver(o1);
		subject.addObserver(o2);
		subject.addObserver(o3);
		subject.addObserver(o4);

		//改变subject的状态
		subject.setState(300);

		System.out.println(o1.getMyState());
		System.out.println(o2.getMyState());
		System.out.println(o3.getMyState());
		System.out.println(o4.getMyState());
	}

}
