package concurrent;

/**
 * nohup java -cp . Thread18 &
 * 日志会输出到本地的nohup.out中
 */
public class Thread18 {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The app will be exit.");
            notifyAndRelease();
        }));
        int i = 0;
        while (true) {
            try {
                Thread.sleep(1_000);
                System.out.println("working");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if (i > 20) {
                throw new RuntimeException("Error");
            }
        }
    }

    private static void notifyAndRelease() {
        System.out.println("通知或唤醒别的线程...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("释放资源...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("notifyAndRelease over");
    }
}
