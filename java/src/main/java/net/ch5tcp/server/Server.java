package net.ch5tcp.server;


import java.io.IOException;
import net.ch5tcp.constants.TCPConstants;

/**
 * 服务提供方
 *
 * @author joyang
 */
public class Server {

    public static void main(String[] args) {
        // 启动一个TCP服务监听，需要传入监听端口
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
        boolean isSucceed = tcpServer.start();
        if (!isSucceed) {
            System.out.println("Start TCP server failed!");
            return;
        }

        // 启动一个UDP服务，接收广播消息
        UDPProvider.start(TCPConstants.PORT_SERVER);

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UDPProvider.stop();
        tcpServer.stop();
    }
}
