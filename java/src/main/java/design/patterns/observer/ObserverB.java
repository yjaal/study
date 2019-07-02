package design.patterns.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者（使用java自带）
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class ObserverB implements Observer {

	private int myState;

	@Override
	public void update(Observable o, Object arg) {
		myState = ((ConcreteSubject1) o).getState();
	}

	public int getMyState() {
		return myState;
	}

	public void setMyState(int myState) {
		this.myState = myState;
	}
}
