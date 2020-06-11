package concurrent.phase2;

public class DBQueryAction {

    public void excute() {
        try {
            Context context = ActionContext.getActionContext().getContext();
            Thread.sleep(1000);
            String name = Thread.currentThread().getName() + "-Alex";
            context.setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
