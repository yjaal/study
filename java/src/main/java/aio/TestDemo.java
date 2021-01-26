package aio;

import aio.client.Client;
import aio.server.Server;
import java.util.Scanner;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class TestDemo {
	//测试主方法
	public static void main(String[] args) throws Exception {
		//运行服务器
		Server.start();
		//避免客户端先于服务器启动前执行代码
		Thread.sleep(100);
		//运行客户端
		Client.start();
		System.out.println("请输入请求消息：");
		Scanner scanner = new Scanner(System.in);
		while (Client.sendMsg(scanner.nextLine())) ;
	}
}
