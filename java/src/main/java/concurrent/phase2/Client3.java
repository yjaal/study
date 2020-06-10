package concurrent.phase2;


public class Client3 {

    public static void main(String[] args) throws Exception {
        FutureService service = new FutureService();
        //提交一个任务
        service.submit(() -> {
            try {
                //do something
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "finish";
        }, System.out::println);

        System.out.println("do other things");
        Thread.sleep(100);
        System.out.println("other things done");
    }
}