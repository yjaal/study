package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author joyang
 */
public class UdpSearcher2 {

    // 监听30000端口
    private static final int LISTEN_PORT = 30000;


    public static void main(String[] args) throws Exception {
        System.out.println("UDP searcher started...");

        // 开启一个监听线程
        Listner listen = listen();
        // 发送广播消息
        sendBroadcast();

        System.out.println("UDP searcher finished...");

        // 读取任意键盘信息后可以退出
        System.in.read();
        List<Device> devices = listen.getDevicesAndClose();
        devices.forEach(device -> System.out.println(device.toString()));
    }

    private static Listner listen() throws InterruptedException {
        System.out.println("UDP searcher start listen...");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listner listner = new Listner(LISTEN_PORT, countDownLatch);
        listner.start();
        countDownLatch.await();
        return listner;
    }

    // 发送广播消息
    private static void sendBroadcast() throws Exception {
        System.out.println("UDP searcher send broadcast started...");

        // 作为搜索方，让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();

        // 构建一份请求数据
        String reqData = MessageCreator.buildWithPort(LISTEN_PORT);
        byte[] reqDataBytes = reqData.getBytes();
        // 直接根据发送者构建一份要发送的信息
        DatagramPacket reqPacket = new DatagramPacket(reqDataBytes, reqDataBytes.length);
        // 这里要填广播地址
        reqPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        // 这里表示要往这个端口发送数据
        reqPacket.setPort(20000);
        // 发送
        ds.send(reqPacket);
        ds.close();

        System.out.println("UDP searcher send broadcast finished...");
    }

    private static class Device {

        final int port;
        final String ip;
        final String sn;

        private Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{ port=" + port + ", ip='" + ip + '\'' +
                ", sn='" + sn + '\'' + '}';
        }
    }

    private static class Listner extends Thread {

        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> devices = new ArrayList<>();
        private boolean done = false;
        private DatagramSocket ds = null;

        public Listner(int listenPort, CountDownLatch countDownLatch) {
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            // 通知已启动
            countDownLatch.countDown();
            try {
                ds = new DatagramSocket(listenPort);
                while (!done) {
                    // 构建接收实体缓存
                    final byte[] buf = new byte[512];
                    DatagramPacket reqPack = new DatagramPacket(buf, buf.length);
                    // 接收会送的数据
                    ds.receive(reqPack);

                    // 打印接收到的信息与发送者的信息
                    String senderIp = reqPack.getAddress().getHostAddress();
                    int senderPort = reqPack.getPort();
                    int dataLen = reqPack.getLength();
                    String data = new String(reqPack.getData(), 0, dataLen);
                    System.out.println("UDP searcher receive from ip: " + senderIp + "\tport: "
                        + senderPort + "\tdata: " + data);

                    String sn = MessageCreator.parseSn(data);
                    if (!Objects.isNull(sn)) {
                        devices.add(new Device(senderPort, senderIp, sn));
                    }
                }
            } catch (Exception e) {
                System.out.println("异常");
            } finally {
                close();
            }
            System.out.println("end");
        }

        private List<Device> getDevicesAndClose() {
            this.done = true;
            close();
            return devices;
        }

        public void close() {
            if (!Objects.isNull(ds)) {
                ds.close();
                ds = null;
            }
        }
    }
}
