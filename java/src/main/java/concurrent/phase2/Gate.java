package concurrent.phase2;

public class Gate {

    private int counter = 0;

    //共享资源
    private String name = "no body";
    private String address = "no where";

    /**
     * 临界值
     */
    public void pass(String otherName, String otherAddress) {
        this.counter++;
        //竞争
        this.name = otherName;
        this.address = otherAddress;
        verify();
    }

    private void verify() {
        if (this.name.charAt(0) != this.address.charAt(0)) {
            System.out.println("------BROKEN----------" + toString());
        }
    }

    @Override
    public String toString() {
        return "Gate{" +
            "counter=" + counter +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            '}';
    }
}
