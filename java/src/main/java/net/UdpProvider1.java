package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author joyang
 */
public class UdpProvider1 {

    public static void main(String[] args) throws IOException {
        System.out.println("UDP provider started...");

        // 作为接收者，指定一个端口用于数据接收
        DatagramSocket ds = new DatagramSocket(20000);

        // 构建接收实体缓存
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

        // 接收
        ds.receive(receivePack);

        // 打印接收到的信息与发送者的信息
        String senderIp = receivePack.getAddress().getHostAddress();
        int senderPort = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDP provider receive from ip: " + senderIp + "\tport: "
            + senderPort + "\tdata: " + data);

        // 构建一份发送的数据
        String respData = "Receive data with len: " + dataLen;
        byte[] respDataBytes = respData.getBytes();

        // 直接根据发送者构建一份发送的信息
        DatagramPacket respPacket = new DatagramPacket(respDataBytes, respDataBytes.length, receivePack.getAddress(),
            senderPort);
        // 发送
        ds.send(respPacket);

        System.out.println("UDP provider finished...");
        ds.close();
    }
}
