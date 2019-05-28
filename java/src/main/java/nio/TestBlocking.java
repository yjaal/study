package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 一、使用NIO完成网络通信的三个核心“
 * 1、通道:
 * java.nio.channels.Channel接口
 * SelectableChannel
 * SocketChannel
 * ServerSocketChannel
 * DatagramChannel
 * ---------------
 * Pipe.SinkChannel
 * Pipe.SourceChannel
 * <p>
 * 2、缓冲区
 * 3、选择器：是SelectableChannel的多路复用器。用于监控SelectableChannel的IO状况
 */
public class TestBlocking {

	public void client1() throws IOException {
		//获取通道
		SocketChannel cChannel = SocketChannel.open(new InetSocketAddress("localhost", 8888));
		//分配指定大小的缓冲区域
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//读取本地文件发送
		FileChannel fileChannel = FileChannel.open(Paths.get("D:\\1.txt"), StandardOpenOption.READ);
		while (fileChannel.read(buf) != -1) {
			buf.flip();
			cChannel.write(buf);
			buf.clear();
		}
		//关闭通道
		cChannel.close();
		fileChannel.close();
	}

	public void server1() throws IOException {
		//获取通道
		ServerSocketChannel sChannel = ServerSocketChannel.open();
		//绑定链接
		sChannel.bind(new InetSocketAddress(8888));
		//获取客户端链接通道
		SocketChannel cChannel = sChannel.accept();
		//接收客户端发送过来的数据
		FileChannel fileChannel = FileChannel.open(Paths.get("D:\\2.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		while (cChannel.read(buf) != -1) {
			buf.flip();
			fileChannel.write(buf);
			buf.clear();
		}
		sChannel.close();
		cChannel.close();
		fileChannel.close();
	}

	/**
	 * 上面例子是一个最简单的实现方式，下面这种方式需要服务端收到消息后
	 * 对客户端进行反馈
	 */
	public void client2() throws IOException {
		SocketChannel cChannel = SocketChannel.open(new InetSocketAddress("localhost", 8888));
		ByteBuffer buf = ByteBuffer.allocate(1024);
		FileChannel fileChannel = FileChannel.open(Paths.get("D:\\1.txt"), StandardOpenOption.READ);
		while (fileChannel.read(buf) != -1) {
			buf.flip();
			cChannel.write(buf);
			buf.clear();
		}

		//接受服务端的反馈
		int len = 0;
		while ((len = cChannel.read(buf)) != -1) {
			buf.flip();
			System.out.println(new String(buf.array(), 0, len));
			buf.clear();
		}
		cChannel.close();
		fileChannel.close();
	}

	public void server2() throws IOException {
		ServerSocketChannel sChannel = ServerSocketChannel.open();
		sChannel.bind(new InetSocketAddress(8888));
		SocketChannel cChannel = sChannel.accept();
		FileChannel fileChannel = FileChannel.open(Paths.get("D:\\2.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		while (cChannel.read(buf) != -1) {
			buf.flip();
			fileChannel.write(buf);
			buf.clear();
		}

		buf.put("接收完毕!".getBytes());
		buf.flip();
		cChannel.write(buf);
		buf.clear();

		sChannel.close();
		cChannel.close();
		fileChannel.close();
	}
}
