package concurrent.phase2;

import java.util.stream.IntStream;

/**
 * 1、所有的对象都有一个wait set， 用来存放该对象 wait 方法之后进入 blocked 状态线程;
 * 2、线程被 notify 之后不一定会立即执行;
 * 3、线程被从 wait set 中唤醒的顺序不一定按照启动的顺序;
 * 4、当wait之后被唤醒时，虽然是需要重新获取锁，但是这里是接着上次被wait的地方执行，而不是重新来过，这样就死循环类
 */
public class WaitSet {

    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 10).forEach(i ->
            new Thread(String.valueOf(i)) {
                @Override
                public void run() {
                    synchronized (LOCK) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " into wait ");
                            LOCK.wait();
                            System.out.println(Thread.currentThread().getName() + " out wait ");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IntStream.rangeClosed(1, 10).forEach(i -> {
                synchronized (LOCK) {
                    //一个一个唤醒
                    LOCK.notify();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        );

    }
}
