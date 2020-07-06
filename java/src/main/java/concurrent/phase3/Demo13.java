package concurrent.phase3;

public class Demo13 {

    public static void main(String[] args) {
        int x = 16;
        int tmp = 1 << x;
        int maxCount = tmp - 1;
        System.out.println(tmp);
        System.out.println(maxCount);
        int y = -16;
        String s = Integer.toBinaryString(y);
        System.out.println(s);
        System.out.println(Math.sqrt(65536));
    }
}
