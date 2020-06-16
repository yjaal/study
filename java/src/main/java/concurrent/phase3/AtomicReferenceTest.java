package concurrent.phase3;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    public static void main(String[] args) {
        AtomicReference<Simple> atomic = new AtomicReference<>(new Simple("Alext", 12));
        System.out.println(atomic.get());

        boolean res = atomic.compareAndSet(new Simple("aa", 13), new Simple("bb", 13));
        System.out.println(res);
    }

    static class Simple {

        private String name;
        private int age;

        public Simple() {
        }

        public Simple(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Simple{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
        }
    }
}
