package concurrent.phase2;


public class Client6 {

    public static void main(String[] args) throws InterruptedException {
        final ProducerData data = new ProducerData();
        new Producer(data, 1).start();
        new Producer(data, 2).start();
        new Producer(data, 3).start();
        new Consumer(data, 1).start();
        new Consumer(data, 2).start();
    }
}
