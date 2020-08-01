package concurrent.phase3;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Demo16 {

    private final static int MAX_THRESHOLD = 3;

    private final static AtomicInteger SUM = new AtomicInteger(0);

    public static void main(String[] args) {
        final ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new CalculatedRecursiveAction(0, 10));
        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
            Optional.of(SUM).ifPresent(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class CalculatedRecursiveAction extends RecursiveAction {

        private final int start;

        private final int end;

        public CalculatedRecursiveAction(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= MAX_THRESHOLD) {
                SUM.addAndGet(IntStream.rangeClosed(start, end).sum());
            } else {
                int middle = start + (end - start) / 2;
                CalculatedRecursiveAction left = new CalculatedRecursiveAction(start, middle);
                CalculatedRecursiveAction right = new CalculatedRecursiveAction(middle + 1, end);
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
        }
    }
}
