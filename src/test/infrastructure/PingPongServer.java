package test.infrastructure;

import esm.distribution.serialization.Marshaller;
import esm.infrastructure.ServerRequestConnector;
import esm.infrastructure.ServerRequestHandler;
import esm.infrastructure.impl.udp.UDPServerRequestConnector;

import java.io.IOException;
import java.net.InetAddress;

/**
 * The PingPong test for the ServerRequestHandler implementations.
 *
 * @author Pedro Henrique
 */
public class PingPongServer {

    /**
     * Tests the communication between the client and server request handlers, should run after the PingPongClient
     * main.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ServerRequestConnector src
                = new UDPServerRequestConnector(InetAddress.getLocalHost(), PingPongTest.SERVER_PORT);

        String pong = "pong";

        for (int i = 0; i < PingPongTest.CLIENT_NUMBER_OF_PINGS_TO_SERVER; i++) {
            ServerRequestHandler srh = src.accept();
            String receivedClientMessage = (String) Marshaller.unmarshall(srh.receive());
            if (PingPongTest.PRINT_PING_PONG_MESSAGES) {
                System.out.println("Server received: " + receivedClientMessage);
            }
            srh.send(Marshaller.marshall(pong));
            if (PingPongTest.PRINT_PING_PONG_MESSAGES) {
                System.out.println("Server: " + pong);
            }
        }

        src.close();
    }
}
