package design.patterns.observer;

import java.util.Observable;

/**
 * 主题（使用java自带）
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class ConcreteSubject1 extends Observable {

	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void set(int state) {
		//目标对象的状态发生了改变
		this.state = state;
		//表示目标对象已经做了更改
		setChanged();
		//通知所有的观察者
		notifyObservers(state);
	}
}
