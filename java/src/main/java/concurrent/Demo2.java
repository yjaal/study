package concurrent;

import java.util.Arrays;
import java.util.Optional;

public class Demo2 {

    public void m2() {
        Arrays.asList(Thread.currentThread().getStackTrace()).stream()
            .filter(e -> !e.isNativeMethod())
            .forEach(e -> Optional.of(e.getClassName() + ":" + e.getMethodName() + ":" + e.getLineNumber())
                .ifPresent(System.out::println));
    }
}
