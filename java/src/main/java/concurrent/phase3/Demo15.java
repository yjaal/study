package concurrent.phase3;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class Demo15 {

    private final static int MAX_THRESHOLD = 3;

    private static class CalculatedRecursiveTask extends RecursiveTask<Integer> {

        private final int start;

        private final int end;

        public CalculatedRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(start, end).sum();
            } else {
                int middle = start + (end - start) / 2;
                CalculatedRecursiveTask left = new CalculatedRecursiveTask(start, middle);
                CalculatedRecursiveTask right = new CalculatedRecursiveTask(middle + 1, end);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }
    }

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        CalculatedRecursiveTask task = new CalculatedRecursiveTask(1, 10);
        ForkJoinTask<Integer> future = forkJoinPool.submit(task);

        if (task.isCompletedAbnormally()) {
            Throwable exception = task.getException();
        }

        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
