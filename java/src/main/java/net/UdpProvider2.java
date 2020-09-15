package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Objects;
import java.util.UUID;

/**
 * @author joyang
 */
public class UdpProvider2 {

    public static void main(String[] args) throws IOException {
        // 生成唯一标识
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
        provider.start();

        // 读取任意键盘信息后可以退出
        System.in.read();
        provider.exit();
    }

    private static class Provider extends Thread {

        private final String sn;
        private DatagramSocket ds = null;

        private boolean done = false;

        public Provider(String sn) {
            this.sn = sn;
        }

        @Override
        public void run() {
            System.out.println("UDP provider started...");

            try {
                // 构建一个监听端口20000，等待接收数据
                ds = new DatagramSocket(20000);
                while (!done) {
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

                    // 下面开始回送，但是回送的端口不能是上面发送端的端口，而应该回送到指定端口
                    int respPort = MessageCreator.parsePort(data);
                    if (respPort != -1) {
                        // 构建一份回送数据，将我们的SN回送过去
                        String respData = MessageCreator.buildWithSn(sn);
                        byte[] respDataBytes = respData.getBytes();

                        // 直接根据发送者构建一份发送的信息
                        DatagramPacket respPacket = new DatagramPacket(respDataBytes, respDataBytes.length, receivePack.getAddress(),
                            respPort);
                        // 发送
                        ds.send(respPacket);
                    }
                }
            } catch (Exception e) {
                System.out.println("异常");
            } finally {
                close();
            }
            System.out.println("UDP provider finished...");
        }

        public void exit() {
            this.done = true;
            this.close();
        }

        public void close() {
            if (!Objects.isNull(ds)) {
                ds.close();
                ds = null;
            }
        }
    }
}
