package concurrent.phase2;

public class BinaryObserver extends Observer1 {

    public BinaryObserver(Subject1 subject) {
        super(subject);
    }

    @Override
    public void update() {
        System.out.println("Binary Observer: " + Integer.toBinaryString(subject.getState()));
    }
}
