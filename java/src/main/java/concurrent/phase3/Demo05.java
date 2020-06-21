package concurrent.phase3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo05 {

    public static void main(String[] args) {
        Event[] events = new Event[]{new Event(1), new Event(2)};
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (Event event : events) {
            List<Table> tables = capture(event);
            for (Table table : tables) {
                TaskBatch taskBatch = new TaskBatch(2);
                TrustSourceColumn trustSourceColumn = new TrustSourceColumn(table, taskBatch);
                TrustSourceRecordCount trustSourceRecordCount = new TrustSourceRecordCount(table, taskBatch);
                pool.submit(trustSourceColumn);
                pool.submit(trustSourceRecordCount);
            }
        }
    }

    interface Watcher {

        void done(Table table);
    }

    static class TaskBatch implements Watcher {

        private CountDownLatch latch;

        public TaskBatch(int size) {
            this.latch = new CountDownLatch(size);
        }

        @Override
        public void done(Table table) {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("The table " + table.tableName + " finished the work,[" + table + "]");
            }
        }
    }

    static class Event {

        int id;

        public Event(int id) {
            this.id = id;
        }
    }

    static class Table {

        String tableName;
        long sourceRecordCount = 10;
        long targetCount;
        String sourceColumnSchema = "<table name = a><column name='coll' type='varchar2'/></table>";
        String targetColumnSchema = "";

        public Table(String tableName, long sourceRecordCount) {
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }

        @Override
        public String toString() {
            return "Table{" +
                "tableName='" + tableName + '\'' +
                ", sourceRecordCount=" + sourceRecordCount +
                ", targetCount=" + targetCount +
                ", sourceColumnSchema='" + sourceColumnSchema + '\'' +
                ", targetColumnSchema='" + targetColumnSchema + '\'' +
                '}';
        }
    }

    private static List<Table> capture(Event event) {
        List<Table> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(new Table("table-" + event.id + "-" + i, i * 1000));
        }
        return list;
    }

    static class TrustSourceColumn implements Runnable {

        private final Table table;
        private TaskBatch taskBatch;

        public TrustSourceColumn(Table table, TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetColumnSchema = table.sourceColumnSchema;
//            System.out.println("The table " + table.tableName + " target columns capture done and update.");
            taskBatch.done(table);
        }
    }

    static class TrustSourceRecordCount implements Runnable {

        private final Table table;
        private TaskBatch taskBatch;

        public TrustSourceRecordCount(Table table, TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetCount = table.sourceRecordCount;
//            System.out.println("The table " + table.tableName + " target record count capture done and update.");
            taskBatch.done(table);
        }
    }

}
