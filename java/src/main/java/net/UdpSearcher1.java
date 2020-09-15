package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author joyang
 */
public class UdpSearcher1 {
    public static void main(String[] args) throws IOException {
        System.out.println("UDP searcher started...");

        // 作为搜索方，让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();

        // 构建一份请求数据
        String reqData = "Hello World";
        byte[] reqDataBytes = reqData.getBytes();
        // 直接根据发送者构建一份要发送的信息
        DatagramPacket reqPacket = new DatagramPacket(reqDataBytes, reqDataBytes.length);
        // 上面没有直接设置地址
        reqPacket.setAddress(InetAddress.getLocalHost());
        // 这里表示要往这个端口发送数据
        reqPacket.setPort(20000);
        // 发送
        ds.send(reqPacket);

        // 构建接收实体缓存
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        // 接收会送的数据
        ds.receive(receivePack);

        // 打印接收到的信息与发送者的信息
        String senderIp = receivePack.getAddress().getHostAddress();
        int senderPort = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDP searcher receive from ip: " + senderIp + "\tport: "
            + senderPort + "\tdata: " + data);

        System.out.println("UDP searcher finished...");
        ds.close();

    }
}
