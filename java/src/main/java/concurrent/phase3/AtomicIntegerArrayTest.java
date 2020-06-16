package concurrent.phase3;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayTest {

    public static void main(String[] args) {
        AtomicIntegerArray arr = new AtomicIntegerArray(10);
        arr.set(0, 10);
        System.out.println(arr.get(0));
        System.out.println(arr.get(1));
    }
}
