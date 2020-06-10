package concurrent.phase2;

/**
 * 有了票据，那怎样才能知道任务是否已经完成了呢？这就需要下面的Task了。
 * 只有任务自己才能知道自己是否已经完成了
 */
public interface MyFutureTask<T> {
    T call();
}
