package concurrent.phase3;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Demo08 {

    public static void main(String[] args) {
        ResourceManage resourceManage = new ResourceManage();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(new ResourceUser(resourceManage, i));
        }
        Arrays.asList(threads).forEach(Thread::start);
    }

    private static class ResourceUser implements Runnable {

        private ResourceManage resourceManage;
        private int userId;

        public ResourceUser(ResourceManage resourceManage, int userId) {
            this.resourceManage = resourceManage;
            this.userId = userId;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ", 准备使用资源...");
            resourceManage.useResource(userId);
            System.out.println(Thread.currentThread().getName() + ", 使用资源完毕...");
        }
    }

    private static class ResourceManage {

        private boolean resources[];
        private final ReentrantLock lock;
        private final Semaphore semaphore;

        public ResourceManage() {
            this.resources = new boolean[10];
            Arrays.fill(this.resources, true);
            //公平锁，先来先进
            this.lock = new ReentrantLock(true);
            //控制10个共享资源使用，FIFO
            this.semaphore = new Semaphore(10, true);
        }

        public void useResource(int userId) {
            try {
                semaphore.acquire();
                //抢占资源
                int id = getResourceId();
                System.out.println(Thread.currentThread().getName() + ", userId:" + userId + " 正在使用资源，资源id: " + id);
                TimeUnit.SECONDS.sleep(10);
                resources[id] = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

        private int getResourceId() {
            int id = -1;
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    if (resources[i]) {
                        resources[i] = false;
                        id = i;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return id;
        }
    }
}
