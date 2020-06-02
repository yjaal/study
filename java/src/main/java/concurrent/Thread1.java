package concurrent;

public class Thread1 {

    private static int count = 0;

    public static void main(String[] args) {
        try {
            add(0);
        } catch (StackOverflowError error) {
            error.printStackTrace();
            System.out.println(count);
        }
    }

    private static void add(int idx) {
        ++count;
        add(idx++);
    }
}
