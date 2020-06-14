package concurrent.phase2;

public class Client9 {

    public static void main(String[] args) {
        CounterIncrement counter = new CounterIncrement();
        counter.start();
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        counter.close();
    }

}
