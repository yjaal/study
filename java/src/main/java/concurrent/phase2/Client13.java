package concurrent.phase2;

public class Client13 {

    private static int x = 0;

    private static int y;

    private static Client13 client13 = new Client13();

    public Client13() {
        x++;
        y++;
    }

    public static Client13 getInstance() {
        return client13;
    }

    public static void main(String[] args) {
        Client13 instance = getInstance();
        System.out.println(instance.x);
        System.out.println(instance.y);
    }
}
