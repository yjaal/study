package concurrent.phase2;

public class UseGate extends Thread{

    private final Gate gate;

    private final String name;

    private final String address;

    public UseGate(String name, String address, Gate gate) {
        this.name = name;
        this.address = address;
        this.gate = gate;
    }

    @Override
    public void run() {
        System.out.println(name + " begin");
        while (true) {
            this.gate.pass(name, address);
        }
    }
}
