package design.patterns.observer;

/**
 * 具体的某个主题类
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class ConcreteSubject extends Subject {

	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		//主题变化后通知所有的观察者
		notifyAll(state);
	}
}
