package concurrent.phase2;

import java.util.stream.IntStream;

public class Client8 {

    public static void main(String[] args) {
        MessageHandler handler = new MessageHandler();
        IntStream.rangeClosed(0, 10).forEach(i -> handler.request(String.valueOf(i)));
        handler.shutdown();
    }
}
