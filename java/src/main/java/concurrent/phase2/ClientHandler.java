package concurrent.phase2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;

    private volatile boolean running = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream ins = socket.getInputStream();
            OutputStream outs = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            PrintWriter printWriter = new PrintWriter(outs)) {
            while (running) {
                String msg = br.readLine();
                if (msg == null) {
                    break;
                }
                System.out.println("Come from client: " + msg);
                printWriter.write("echo: " + msg);
                printWriter.flush();
            }
        } catch (Exception e) {
            this.running = false;
        } finally {
            this.stop();
        }
    }

    public void stop() {
        System.out.println("client stop");
        if (!running) {
            return;
        }
        this.running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
