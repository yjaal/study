package concurrent.phase2;

public class MakeClientThread extends Thread {

    private final ActiveObj activeObj;

    private final char fillChar;


    public MakeClientThread(String name, ActiveObj activeObj) {
        super(name);
        this.activeObj = activeObj;
        this.fillChar = name.charAt(0);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                Result result = activeObj.makeString(i + 1, fillChar);
                Thread.sleep(200);
                String resultVal = (String) result.getResultVal();
                System.out.println(Thread.currentThread().getName() + ": value=" + resultVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
