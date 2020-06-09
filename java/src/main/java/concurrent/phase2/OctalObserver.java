package concurrent.phase2;

public class OctalObserver extends Observer1 {

    public OctalObserver(Subject1 subject) {
        super(subject);
    }

    @Override
    public void update() {
        System.out.println("Octal Observer: " + Integer.toOctalString(subject.getState()));
    }
}
