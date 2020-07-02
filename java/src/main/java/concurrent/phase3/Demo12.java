package concurrent.phase3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Demo12 {

    static Map<String, Object> datas = new HashMap<>();
    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock r = lock.readLock();
    static Lock w = lock.writeLock();

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                setData(i + "", i + "");
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                getData(i + "");
            }
        }).start();
    }

    public static void setData(String key, String value) {
        w.lock();
        try {
            datas.put(key, value);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("写入的值是:" + key + "<->" + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            w.unlock();
        }
    }

    public static void getData(String key) {
        r.lock();
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("读取的值是：" + key + "<->" + datas.get(key));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            r.unlock();
        }
    }

}
