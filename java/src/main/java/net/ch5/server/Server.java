package net.ch5.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import net.ch5.constants.TCPConstants;

public class Server {
    public static void main(String[] args) throws IOException {
        // 启动一个TCP监听
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
        boolean isSucceed = tcpServer.start();
        if (!isSucceed) {
            System.out.println("Start TCP server failed!");
            return;
        }

        // 接收广播消息
        UDPProvider.start(TCPConstants.PORT_SERVER);

        // 读取键盘输入
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = bufferedReader.readLine();
            // 给连接的所有客户端都发送消息
            tcpServer.broadcast(str);
        } while (!"00bye00".equalsIgnoreCase(str));

        UDPProvider.stop();
        tcpServer.stop();
    }
}
