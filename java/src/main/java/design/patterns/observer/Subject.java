package design.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题类
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class Subject {

	List<Observer> observers = new ArrayList<>();

	public void register(Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	public void remove(Observer observer) {
		if (!observers.isEmpty()) {
			observers.remove(observer);
		}
	}

	public void notifyAll(int state) {
		observers.forEach(observer -> observer.update(this));
	}
}
