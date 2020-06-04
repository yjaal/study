package concurrent;

import java.util.stream.Stream;

public class Thread13 {

    public static void main(String[] args) {
        ProducerConsumer1 pc = new ProducerConsumer1();
        Stream.of("生产线程1", "生产线程2").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.producer();
                }
            }, name).start());

        Stream.of("消费线程1", "消费线程2").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.consumer();
                }
            }, name).start());
    }
}
