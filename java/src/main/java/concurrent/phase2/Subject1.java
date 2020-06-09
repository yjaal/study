package concurrent.phase2;

import java.util.ArrayList;
import java.util.List;

public class Subject1 {

    private List<Observer1> observers = new ArrayList<>();

    private int state;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        if (state == this.state) {
            return;
        }
        this.state = state;
        notifyAllObservers();
    }

    public void attach(Observer1 observer) {
        observers.add(observer);
    }

    private void notifyAllObservers() {
        observers.forEach(Observer1::update);
    }
}
