package concurrent.phase2;

/**
 * 接受异步消息的主动方法
 */
public interface ActiveObj {

    Result makeString(int count, char fillChar);

    void displayString(String text);
}
