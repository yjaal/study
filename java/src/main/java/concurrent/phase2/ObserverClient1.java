package concurrent.phase2;

public class ObserverClient1 {

    public static void main(String[] args) {
        final Subject1 subject = new Subject1();
        BinaryObserver binaryObserver = new BinaryObserver(subject);
        OctalObserver octalObserver = new OctalObserver(subject);
        System.out.println("--------------");
        subject.setState(10);
    }
}
