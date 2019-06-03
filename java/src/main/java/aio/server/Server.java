package aio.server;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class Server {

	private static int DEFAULT_PORT = 8888;
	private static AsyncServerHandler serverHandle;
	public volatile static long clientCount = 0;

	public static void start() {
		start(DEFAULT_PORT);
	}

	public static synchronized void start(int port) {
		if (serverHandle != null) {
			return;
		}
		serverHandle = new AsyncServerHandler(port);
		new Thread(serverHandle, "Server").start();
	}

	public static void main(String[] args) {
		Server.start();
	}
}
