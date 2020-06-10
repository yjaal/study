package concurrent.phase2;

public class Request {

    private final String value;

    public Request(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
