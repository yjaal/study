package concurrent.phase2;

public class ChannelRequest {

    /**
     * 谁放上去的
     */
    private final String name;

    /**
     * 放在哪个位置
     */
    private final int number;

    public ChannelRequest(String name, int number) {
        this.name = name;
        this.number = number;
    }


    /**
     * 货物怎么放置
     */
    public void execute() {
        System.out.println(Thread.currentThread().getName() + " execute " + this);
    }

    @Override
    public String toString() {
        return "ChannelRequest{name='" + name + '\'' +
            ", number=" + number +
            '}';
    }
}
