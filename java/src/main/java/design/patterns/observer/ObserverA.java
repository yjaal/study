package design.patterns.observer;

/**
 * 观察者A
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class ObserverA implements Observer {

	private int myState;

	public int getMyState() {
		return myState;
	}

	public void setMyState(int myState) {
		this.myState = myState;
	}

	@Override
	public void update(Subject subject) {
		this.myState = ((ConcreteSubject)subject).getState();
	}
}
