package nio;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * @description nio buffer
 * 一、缓冲区（buffer）：在Java NIO中负责数据的存取。缓冲区本质是数组，用于存储不同的数据类型的数据。非线程安全
 * 根据数据类型不同（boolean除外），提供了相应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 *
 * 二、缓冲区存储数据的两个核心方法：
 * put():存入数据到缓冲区中
 * get():获取缓冲区中的数据
 *
 * 三、缓冲区的四个核心属性
 * capacity：容量。一旦声明不能改变
 * limit：界限。表示缓冲区中的可以操作数据的大小（界限）。即limit后面的位置是不能读写的
 * position：位置。表示缓冲区中正在操作的数据的位置。0 <= mark <= position <= limit <= capacity
 * mark：标记。表示记录当前的position的位置。通过reset方法恢复到mark的位置
 *
 * 四、直接缓冲区与非直接缓冲区
 * 非直接缓冲区：通过allocate方法分配缓冲区，将缓冲区建立在JVM内存中
 * 直接缓冲区：通过allocateDirect方法分配缓冲区，将缓冲区建立在操作系统的物理内存中，可以提高效率
 *           但是和物理磁盘建立的引用不容易销毁，除非垃圾回收机制进行回收
 */
public class TestBuffer {

	public static void main(String[] args) {
		TestBuffer obj = new TestBuffer();
		obj.test1();
	}

	/**
	 * 基本操作
	 */
	public void test1() {
		String str = "aaaa";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		//[pos=0 lim=1024 cap=1024]
		System.out.println("1. " + buffer);
		//往缓冲区中写数据
		buffer.put(str.getBytes());
		//[pos=4 lim=1024 cap=1024]
		System.out.println("2. " + buffer);
		//切换读写模式
		buffer.flip();
		//[pos=0 lim=4 cap=1024]
		System.out.println("3. " + buffer);
		byte[] bytes = new byte[buffer.limit()];
		//从缓冲区中取数据
		buffer.get(bytes);
		//[pos=4 lim=4 cap=1024]
		System.out.println("4. " + buffer);
		System.out.println(Arrays.toString(bytes));

		//使得buffer可重复读数据，pos又回到了0位置
		buffer.rewind();
		//[pos=0 lim=4 cap=1024]
		System.out.println("5. " + buffer);

		//清空缓冲区，但是缓冲区的数据依然存在
		buffer.clear();
		//[pos=0 lim=1024 cap=1024]
		System.out.println("6. " + buffer);
	}
}
