package concurrent.phase1;

public class Demo3 {

    public void m2(MyInteface myInteface) {
        //something
        myInteface.m1();
        //something
    }

    public static void main(String[] args) {
        Demo3 d = new Demo3();
        d.m2(new MyImpl1());
        d.m2(new MyImpl2());
    }
}
