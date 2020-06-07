package concurrent.phase2;

public class Singleton6 {

    private Singleton6() {
        //empty
    }

    public static Singleton6 getInstance() {
        return Singleton.INTANCE.getInstance();
    }

    private enum Singleton {
        INTANCE;

        private final Singleton6 instance;

        Singleton() {
            instance = new Singleton6();
        }

        public Singleton6 getInstance() {
            return instance;
        }
    }
}
