package concurrent.phase2;

/**
 * 这里定义一个任务，这个任务实际业务逻辑由QueryAction来完成
 */
public class ExecutionTask implements Runnable {

    private DBQueryAction dbQueryAction = new DBQueryAction();
    private HttpQueryAction httpQueryAction = new HttpQueryAction();

    @Override
    public void run() {
        final Context context = ActionContext.getActionContext().getContext();
        dbQueryAction.excute();
        System.out.println("DBQueryAction success");
        httpQueryAction.excute();
        System.out.println("httpQueryAction success");
        System.out.println("The username is: " + context.getName()
            + " and card id is:" + context.getCardId());
    }
}
