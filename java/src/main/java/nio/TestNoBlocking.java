package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class TestNoBlocking {

	public void client1() throws IOException {
		//获取通道
		SocketChannel cChannel = SocketChannel.open(new InetSocketAddress("localhost", 8888));
		//切换为非阻塞模式
		cChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(LocalDateTime.now().toString().getBytes());
		buf.flip();
		cChannel.write(buf);
		buf.clear();
		cChannel.close();
	}

	public void server1() throws IOException {
		ServerSocketChannel sChannel = ServerSocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.bind(new InetSocketAddress(8888));
		//获取选择器
		Selector selector = Selector.open();
		//将通道注册到选择器上，第二个参数表示让选择器监听通道的什么状态，下面是指定为接收状态
		sChannel.register(selector, SelectionKey.OP_ACCEPT);
		//通多选择器轮询获取选择器上已经准备就绪的事件
		while (selector.select() > 0) {
			//获取到了所有选择器上的key迭代器
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				//针对不同的事件进行不同的处理
				if (key.isAcceptable()) {
					//获取客户端的连接
					SocketChannel cChannel = sChannel.accept();
					//客户端连接切换非阻塞模式
					cChannel.configureBlocking(false);
					//将该通道注册到选择器上
					cChannel.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					//获取读就绪状态的通道
					SocketChannel readChannel = (SocketChannel) key.channel();
					//读数据
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int len = 0;
					while ((len = readChannel.read(buf)) != -1) {
						buf.flip();
						readChannel.write(buf);
						buf.clear();
					}
				}
				//操作完之后一定要从选择器上将key删除
				iterator.remove();
			}
		}
		sChannel.close();
	}

}
