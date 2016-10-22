package test.infrastructure;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Contains the properties to configure the execution of the PingPongClient and PingPongServer main methods.
 *
 * @author Pedro Henrique
 */
public final class PingPongTest {

    /**
     * The server address, can be changed in the static initializer.
     */
    static InetAddress SERVER_ADDRESS;

    static {
        try {
            SERVER_ADDRESS = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * The server port.
     */
    static final int SERVER_PORT = 50000;

    /**
     * The client ping interval in milliseconds.
     */
    static final int CLIENT_PING_INTERVAL_MILLISECONDS = 100;

    /**
     * The number of pings to be send by the client.
     */
    static final int CLIENT_NUMBER_OF_PINGS_TO_SERVER = 20;

    /**
     * Indicates to print the ping pong messages.
     */
    static final boolean PRINT_PING_PONG_MESSAGES = true;

    /**
     * Runs the PingPongClient and PingPongServer in separated threads. The System.out is shared by the threads.
     *
     * @param args do nothing
     */
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                PingPongServer.main(null);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            try {
                PingPongClient.main(null);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
