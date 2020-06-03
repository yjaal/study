package concurrent;


public class Thread8 {

    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();
        service.execute(() -> {
            while (true) {
                //do something
            }
        });
        //这里对上面的while任务设置一个超时时间
        service.shutdown(1000);
        long end = System.currentTimeMillis();
        System.out.println("超时停止：" + (end - start));
    }
}
