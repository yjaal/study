package concurrent.phase2;

import java.util.function.Consumer;

public class FutureService {

    public <T> MyFuture<T> submit(final MyFutureTask<T> task) {
        AsynFuture<T> future = new AsynFuture<>();
        new Thread(() -> {
            //只有任务自己才能知道自己是否已经完成了
            T result = task.call();
            //任务完成后将结果通知给票据，后面我们从票据中就可以拿到结果了
            future.done(result);
        }).start();
        return future;
    }

    public <T> MyFuture<T> submit(final MyFutureTask<T> task, Consumer<T> consumer) {
        AsynFuture<T> future = new AsynFuture<>();
        new Thread(() -> {
            //只有任务自己才能知道自己是否已经完成了
            T result = task.call();
            //任务完成后将结果通知给票据，后面我们从票据中就可以拿到结果了
            future.done(result);
            consumer.accept(result);
        }).start();
        return future;
    }
}
