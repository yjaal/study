package concurrent.phase3;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Demo07 {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " start");
                String exchange = exchanger.exchange(" I come from " + Thread.currentThread().getName());
                System.out.println("Hi " + Thread.currentThread().getName() + exchange);
                System.out.println(Thread.currentThread().getName() + " end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread1").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " start");
                TimeUnit.SECONDS.sleep(10);
                String exchange = exchanger.exchange(" I come from " + Thread.currentThread().getName());
                System.out.println("Hi " + Thread.currentThread().getName() + exchange);
                System.out.println(Thread.currentThread().getName() + " end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread2").start();
    }

}
