package concurrent.phase2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppServer extends Thread {

    private int port;

    private static final int DEFAULT_PORT = 12722;

    private volatile boolean start = true;

    private List<ClientHandler> clientHandlers = new ArrayList<>();

    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    private ServerSocket server;

    public AppServer() {
        this(DEFAULT_PORT);
    }

    public AppServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            while (start) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                pool.submit(clientHandler);
                clientHandlers.add(clientHandler);
            }
        } catch (IOException e) {
            this.shutdown();
            throw new RuntimeException(e);
        } finally {
            this.dispose();
        }
    }

    /**
     * 清理工作
     */
    private void dispose() {
        System.out.println("dispose");
        //清理的时候所有的客户端都需要关闭
        clientHandlers.forEach(ClientHandler::stop);
        this.pool.shutdown();
    }

    /**
     * 外部将其关闭
     */
    public void shutdown() {
        System.out.println("shutdown");
        this.start = false;
        this.interrupt();
        try {
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
