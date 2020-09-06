package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 8888), 3000);

        System.out.println("发起服务器连接， 并进入到后续流程");
        System.out.println("客户端信息： " + socket.getLocalAddress() + " P: " + socket.getLocalPort());
        System.out.println("服务端信息： " + socket.getInetAddress() + " P: " + socket.getPort());

        try {
            handle(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }
        socket.close();
        System.out.println("客户端退出");
    }


    private static void handle(Socket client) throws Exception {
        //键盘输入流
        InputStream inStream = System.in;
        BufferedReader keyboardIn = new BufferedReader(new InputStreamReader(inStream));

        // socket输出流
        OutputStream outStream = client.getOutputStream();
        PrintStream out = new PrintStream(outStream);

        // socket输入流，用于获取服务端返回
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        boolean flag = true;
        do {
            String line = keyboardIn.readLine();
            // 发送数据
            out.println(line);
            // 从服务器端读取一行
            String resp = in.readLine();
            if ("bye".equalsIgnoreCase(resp)) {
                flag = false;
            } else {
                System.out.println("服务端返回： " + resp);
            }
        } while (flag);

        keyboardIn.close();
        in.close();
        out.close();
        System.out.println("客户端断开");
    }

}
