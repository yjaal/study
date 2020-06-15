package concurrent.phase1;

public class ThreadService {

    private Thread executeThread;

    private boolean finished = false;

    public void execute(Runnable task) {
        executeThread = new Thread(() -> {
            Thread runner = new Thread(task);
            runner.setDaemon(true);
            runner.start();
            try {
                runner.join();
                finished = true;
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        });
        executeThread.start();
    }

    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if (System.currentTimeMillis() - currentTime >= mills) {
                //超时结束
                executeThread.interrupt();
                break;
            }
            try {
                //这里短暂休眠一下
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("执行线程将executeThread打断");
                break;
            }
        }
        finished = false;
    }
}
