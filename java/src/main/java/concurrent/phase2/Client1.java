package concurrent.phase2;

public class Client1 {

    public static void main(String[] args) {
        Gate gate = new Gate();
        UseGate bj = new UseGate("Baobao", "Beijing", gate);
        UseGate sh = new UseGate("Shanglao", "Shanghai", gate);
        UseGate gz = new UseGate("Guanglao", "Guangzhou", gate);

        bj.start();
        sh.start();
        gz.start();
    }
}
