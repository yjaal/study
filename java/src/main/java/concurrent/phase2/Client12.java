package concurrent.phase2;

public class Client12 {

    public static void main(String[] args) {
        ActiveObj activeObj = ActiveObjFactory.createActiveObj();

        //发命令
        new MakeClientThread("Alice", activeObj).start();
        new MakeClientThread("Jack", activeObj).start();
        new MakeClientThread("Tom", activeObj).start();

        new DisplayClientThread("Chris", activeObj).start();
    }
}
