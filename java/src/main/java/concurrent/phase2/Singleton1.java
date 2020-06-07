package concurrent.phase2;

public class Singleton1 {

    private static final Singleton1 instance = new Singleton1();

    private Singleton1() {
        //empty
    }

    public static Singleton1 getInstance() {
        return instance;
    }
}
