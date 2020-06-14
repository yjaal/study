package concurrent.phase2;

public class RealResult implements Result {

    private final Object resultVal;

    public RealResult(Object resultVal) {
        this.resultVal = resultVal;
    }

    @Override
    public Object getResultVal() {
        return resultVal;
    }
}
