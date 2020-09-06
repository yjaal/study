package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8888);
        System.out.println("服务器准备就绪");
        System.out.println("服务器信息： " + server.getInetAddress() + " P: " + server.getLocalPort());

        for (; ; ) {
            Socket client = server.accept();
            ClientHandler clientHandler = new ClientHandler(client);
            clientHandler.start();
        }
    }

    private static class ClientHandler extends Thread {

        private Socket socket;
        private boolean flag = true;


        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("新客户端连接： " + socket.getInetAddress() + " P: " + socket.getPort());
            try {
                // 得到打印流，用户数据输出，服务器回送数据使用
                PrintStream socketOut = new PrintStream(socket.getOutputStream());
                // 得到输入流，用于接收数据
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    String str = socketIn.readLine();
                    if ("bye".equalsIgnoreCase(str)) {
                        flag = false;
                        socketOut.println(str);
                    } else {
                        System.out.println(str);
                        socketOut.println("回送数据长度: " + str.length());
                    }
                } while (flag);
                socketIn.close();
                socketOut.close();
            } catch (Exception e) {
                System.out.println("异常断开连接");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端退出: " + socket.getInetAddress() + " P: " + socket.getPort());
        }
    }

}
