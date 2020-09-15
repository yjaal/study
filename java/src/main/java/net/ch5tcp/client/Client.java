package net.ch5tcp.client;

import java.io.IOException;
import net.ch5tcp.client.bean.ServerInfo;

public class Client {

    public static void main(String[] args) {
        // 1、启动UDP服务，发送广播消息
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if (info != null) {
            try {
                // 2、根据UDP回送的服务端信息建立一个TCP点对点连接
                TCPClient.linkWith(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
