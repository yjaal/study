package concurrent.phase2;

import java.util.LinkedList;

public class ProducerData {

    private final LinkedList<String> data;

    private final int limit;

    private final static int MAX = 100;

    public ProducerData() {
        this(MAX);
    }

    public ProducerData(int limit) {
        this.data = new LinkedList<>();
        this.limit = limit;
    }

    public void put(String content) {
        synchronized (data) {
            while (data.size() >= limit) {
                try {
                    data.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data.addLast(content);
            data.notifyAll();
        }
    }

    public String take() {
        synchronized (data) {
            while (data.isEmpty()) {
                try {
                    data.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String res = data.removeFirst();
            data.notifyAll();
            return res;
        }
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        //这里要加锁
        synchronized (data) {
            return data.size();
        }
    }
}
