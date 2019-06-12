package design.patterns;

import java.io.*;

/**
 * 单例模式使用序列化/反序列化破解
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class SingletonTest implements Serializable {

	private static final long serialVersionUID = -2840105177379540912L;

	private static SingletonTest instance = new SingletonTest();

	private SingletonTest() {
	}

	public static SingletonTest getInstance() {
		return instance;
	}

	/**
	 * 在反序列化时，如果我们定义了这个方法，则会直接调用这个方法返回当前的对象，而不是实例化一个新的对象
	 */
	private Object readResolve() throws ObjectStreamException {
		return instance;
	}

	public static void main(String[] args) throws Exception {
		//加反序列化
		//序列化：将s1对象写到磁盘上
		SingletonTest s1 = SingletonTest.getInstance();
		FileOutputStream fos = new FileOutputStream("d:/a.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(s1);
		oos.close();
		fos.close();

		//反序列化
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("d:/a.txt"));
		SingletonTest s2 = (SingletonTest) ois.readObject();
		System.out.println(s1 == s2);

		//防止反序列化，定义readResolve方法
		SingletonTest s3 = SingletonTest.getInstance();
		//序列化：将s1对象写到磁盘上
		FileOutputStream fos1 = new FileOutputStream("d:/b.txt");
		ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
		oos1.writeObject(s3);
		oos1.close();
		fos1.close();

		//反序列化
		ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("d:/b.txt"));
		SingletonTest s4 = (SingletonTest) ois1.readObject();
		System.out.println(s3 == s4);
	}
}
