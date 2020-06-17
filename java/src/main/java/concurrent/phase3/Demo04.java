package concurrent.phase3;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class Demo04 {

    public static void main(String[] args) throws Exception {
        //此种情况下类会被初始化
        // Simple simple = Simple.class.newInstance();

        //这样则不会被初始化
        //Class.forName("concurrent.phase3.Demo04$Simple");

        //这样也可以绕过初始化
        Unsafe unsafe = getUnsafe();
        //直接开辟了一块内存
        Simple simple = (Simple) unsafe.allocateInstance(Simple.class);
        System.out.println(simple.getI());
        System.out.println("Simple类加载器： " + simple.getClass().getClassLoader());

        System.out.println("-----------------");

        Guard guard = new Guard();
        //此时是不会输出的
        guard.work();
        //这里通过Unsafe改变内存中ACCESS_ALLOWED的值
        Field field = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        unsafe.putInt(guard, unsafe.objectFieldOffset(field), 42);
        guard.work();

        System.out.println("-----------------");

        //使用Unsafe跳过类加载器加载一个类，不过会初始化
        byte[] bytes = loadClassContent();
        Class<?> clazz = unsafe.defineClass(null, bytes, 0, bytes.length, null, null);
        Integer value = (Integer) clazz.getMethod("getI").invoke(clazz.newInstance(), null);
        System.out.println(value);

        System.out.println("-----------------");
    }

    private static byte[] loadClassContent() throws Exception{
        File file = new File("D:\\Simple.class");
        FileInputStream fis = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        fis.read(content);
        fis.close();
        return content;
    }

    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class Simple {

        private long i = 0;

        public Simple() {
            this.i = 1;
            System.out.println("初始化: " + i);
        }

        public long getI() {
            return i;
        }
    }

    private static class Guard {

        private int ACCESS_ALLOWED = 1;

        private boolean allow() {
            return 42 == ACCESS_ALLOWED;
        }

        public void work() {
            if (allow()) {
                System.out.println("允许执行");
            }
        }

    }
}
