package concurrent.phase1;

import java.util.stream.Stream;

public class Thread12 {

    public static void main(String[] args) {
        ProducerConsumer1 pc = new ProducerConsumer1();
        Stream.of("生产线程1", "生产线程2", "生产线程3").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.producer();
                }
            }, name).start());

        Stream.of("消费线程1", "消费线程2", "消费线程3", "消费线程4").forEach(name ->
            new Thread(() -> {
                while (true) {
                    pc.consumer();
                }
            }, name).start());

    }
}
