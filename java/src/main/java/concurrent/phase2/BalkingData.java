package concurrent.phase2;

/**
 * 比如一个文件需要保存
 */
public class BalkingData {

    private final String fileName;

    private String content;

    private boolean changed;

    public BalkingData(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.changed = true;
    }

    /**
     * 当收到新的内容时就将changed置为true，表示需要保存，注意此方法需要和
     * save方法成对出现，不然前面的内容会被覆盖掉
     */
    public synchronized void change(String newContent) {
        this.content = newContent;
        this.changed = true;
    }

    /**
     * 这里就相当于查看，如果文件没有收到新内容就直接返回了
     */
    public synchronized void save() {
        if (!changed) {
            return;
        }
        doSave();
        this.changed = false;
    }

    private void doSave() {
        System.out.println(Thread.currentThread().getName()
            + " content[" + content + "] has been saved");
    }
}
