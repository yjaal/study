package concurrent.phase2;

public class Client11 {

    public static void main(String[] args) {
        //传动带启动，共有5个工人
        Channel channel = new Channel(5);
        channel.startWorker();
        //有几个人往传送带上放东西
        new TransportThread("Alex", channel).start();
        new TransportThread("Tom", channel).start();
        new TransportThread("Jack", channel).start();
    }

}
