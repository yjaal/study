package concurrent.phase2;

import java.util.Arrays;

public class ObserverClient2 {

    public static void main(String[] args) {
        ThreadLifeCycleObserver observer = new ThreadLifeCycleObserver();
        observer.concurrentQuery(Arrays.asList("1", "2"));
    }
}
