package concurrent.phase2;

public class DisplayClientThread extends  Thread {

    private final ActiveObj activeObj;

    public DisplayClientThread(String name, ActiveObj activeObj) {
        super(name);
        this.activeObj = activeObj;
    }

    @Override
    public void run() {
        for (int i = 0; true; i++) {
            String text = Thread.currentThread().getName() + "->" + i;
            activeObj.displayString(text);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
