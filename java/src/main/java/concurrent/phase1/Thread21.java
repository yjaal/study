package concurrent.phase1;

public class Thread21 {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getThreadGroup());

        ThreadGroup tg1 = new ThreadGroup("TG1");
        Thread t1 = new Thread(tg1, "t1"){
            @Override
            public void run() {
                System.out.println(getThreadGroup().getName());
                System.out.println(getThreadGroup().getParent().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        ThreadGroup tg2 = new ThreadGroup(tg1, "TG2");
        System.out.println(tg2.getName());
        System.out.println(tg2.getParent().getName());
    }
}
