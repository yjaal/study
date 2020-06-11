package concurrent.phase2;

import java.util.stream.IntStream;

public class Client5 {

    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i -> new Thread(new ExecutionTask()).start());
    }
}
