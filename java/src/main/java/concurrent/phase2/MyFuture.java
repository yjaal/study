package concurrent.phase2;

/**
 * Future 相当于一个票据或者凭证，用于后面获取相关结果
 */
public interface MyFuture<T> {
    T get() throws InterruptedException;
}
