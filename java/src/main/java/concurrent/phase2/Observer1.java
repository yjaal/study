package concurrent.phase2;

public abstract class Observer1 {

    protected Subject1 subject;

    public Observer1(Subject1 subject) {
        this.subject = subject;
        subject.attach(this);
    }

    public abstract void update();

}
