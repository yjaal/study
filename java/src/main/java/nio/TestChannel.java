package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * @description nio Channel
 * 一、通道：用于源节点与目标节点的连接，在Java NIO中，负责缓冲区中数据的传输。其本身不存储任何数据，需要配合缓冲区进行传输
 * <p>
 * 二、通道的主要实现类
 * java.nio.channels.Channel接口：
 * FileChannel（用于本地）
 * SocketChannel（用于网络TCP）
 * ServerSocketChannel（用于网络TCP）
 * DatagramChannel（用于网络UDP）
 * <p>
 * 三、获取通道
 * 1、java针对支持通道的类提供了getChannel方法
 * 本地IO：
 * FileInputStream/FileOutputStream/RandomAccessFile
 * 网络IO：
 * Socket
 * ServerSocket
 * DatagramSocket
 * 2、在JDK1.7中的NIO.2针对各个通道提供了静态方法open()方法
 * 3、在JDK1.7中的NIO.2的Files工具类的newByteChannel()方法
 * <p>
 * 四、通道之间的数据传输
 * transferFrom()、transferTo()
 * <p>
 * 五、分散（Scatter）与聚集（Gather）
 * 分散读取（Scattering Reads）：将一个通道中的数据分散到多个缓冲区中去
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到一个通道中
 * <p>
 * 六、字符集：CharSet
 * 编码：字符串-->字节数组
 * 解码：字节数组-->字符串
 */
public class TestChannel {
	public static void main(String[] args) throws Exception {
		TestChannel obj = new TestChannel();
		obj.test5();
	}


	/**
	 * 使用channel完成一个文件的复制，这是一种最简单的复制方式
	 */
	public void test1() {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new FileInputStream("D:\\1.txt").getChannel();
			outChannel = new FileOutputStream("D:\\2.txt").getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (inChannel.read(buffer) != -1) {
				buffer.flip();
				outChannel.write(buffer);
				buffer.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 使用内存映射进行文件复制
	 */
	public void test2() throws IOException {
		Instant start = Instant.now();
		//后一个参数规定文件的读写权限
		FileChannel inChannel = FileChannel.open(Paths.get("D:\\1.txt"), StandardOpenOption.READ);
		//CREATE_NEW表示一定要新创建，而CREATE表示没有才创建
		FileChannel outChannel = FileChannel.open(Paths.get("D:\\2.txt"), StandardOpenOption.WRITE,
				StandardOpenOption.READ, StandardOpenOption.CREATE);

		//这是一个内存映射，和ByteBuffer.allocateDirect(1024)是一样的道理
		//内存映射只有ByteBuffer支持
		MappedByteBuffer inMappedByteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		//因为这里是读写模式，所以上面的outChannel也必须要有读模式
		MappedByteBuffer outMappedByteBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

		byte[] bytes = new byte[inMappedByteBuffer.limit()];
		inMappedByteBuffer.get(bytes);
		outMappedByteBuffer.put(bytes);

		inChannel.close();
		outChannel.close();
		Instant end = Instant.now();
		System.out.println("耗时： " + Duration.between(start, end));
	}

	/**
	 * 通道之间直接传输
	 */
	public void test3() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("D:\\1.txt"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("D:\\2.txt"), StandardOpenOption.WRITE,
				StandardOpenOption.READ, StandardOpenOption.CREATE);

//		inChannel.transferTo(0, inChannel.size(), outChannel);
		//和上面一样
		outChannel.transferFrom(inChannel, 0, inChannel.size());

		inChannel.close();
		outChannel.close();
	}

	//分散读取
	public void test4() throws IOException {
		RandomAccessFile in = new RandomAccessFile("D:\\1.txt", "rw");
		ByteBuffer[] buffers = new ByteBuffer[]{ByteBuffer.allocate(500), ByteBuffer.allocate(1024)};
		FileChannel inChannel = in.getChannel();
		inChannel.read(buffers);
		for (ByteBuffer buf : buffers) {
			buf.flip();
		}
		RandomAccessFile out = new RandomAccessFile("D:\\2.txt", "rw");
		FileChannel outChannel = out.getChannel();
		outChannel.write(buffers);

		inChannel.close();
		outChannel.close();
		in.close();
		out.close();
	}

	//字符集编码与解码
	public void test5() throws CharacterCodingException {
		Charset cst = Charset.forName("GBK");
		CharsetEncoder encode = cst.newEncoder();
		CharsetDecoder decode = cst.newDecoder();

		CharBuffer buffer = CharBuffer.allocate(1024);
		buffer.put("狗蛋");
		buffer.flip();
		//编码
		ByteBuffer byteBuffer = encode.encode(buffer);
		for (int i = 0; i < byteBuffer.limit(); i++) {
			System.out.println(byteBuffer.get());
		}

		byteBuffer.flip();
		CharBuffer charBuffer = decode.decode(byteBuffer);
		System.out.println(charBuffer.toString());
	}
}