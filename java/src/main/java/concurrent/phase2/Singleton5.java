package concurrent.phase2;

public class Singleton5 {

    private Singleton5() {
        //empty
    }

    private static class InstanceHolder{
        private final static Singleton5 instance = new Singleton5();
    }

    public static Singleton5 getInstance() {
        return InstanceHolder.instance;
    }
}
