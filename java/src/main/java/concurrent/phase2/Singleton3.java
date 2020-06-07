package concurrent.phase2;

public class Singleton3 {

    private static Singleton3 instance;

    private Singleton3() {
        //empty
    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }
}
