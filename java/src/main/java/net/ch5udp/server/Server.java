package net.ch5udp.server;


import java.io.IOException;
import net.ch5udp.constants.TCPConstants;

/**
 * @author joyang
 */
public class Server {

    public static void main(String[] args) {
        ServerProvider.start(TCPConstants.PORT_SERVER);

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerProvider.stop();
    }
}
