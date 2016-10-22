package test.infrastructure;

import esm.distribution.serialization.Marshaller;
import esm.infrastructure.ClientRequestHandler;
import esm.infrastructure.impl.udp.UDPClientRequestHandler;

import java.io.IOException;

/**
 * The PingPong test for the ClientRequestHandler implementations.
 *
 * @author Pedro Henrique
 */
public class PingPongClient {

    /**
     * Tests the communication between the client and server request handlers
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        String ping = "ping";

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < PingPongTest.CLIENT_NUMBER_OF_PINGS_TO_SERVER; i++) {
            ClientRequestHandler crh
                    = new UDPClientRequestHandler(PingPongTest.SERVER_ADDRESS, PingPongTest.SERVER_PORT);
            crh.connect();
            crh.send(Marshaller.marshall(ping));

            if (PingPongTest.PRINT_PING_PONG_MESSAGES) {
                System.out.println("Client: " + ping);
            }
            String receivedServerMessage = (String) Marshaller.unmarshall(crh.receive());
            if (PingPongTest.PRINT_PING_PONG_MESSAGES) {
                System.out.println("Client received: " + receivedServerMessage);
            }
            Thread.sleep(PingPongTest.CLIENT_PING_INTERVAL_MILLISECONDS);
        }

        System.out.println((System.currentTimeMillis() - startTime) / 1000.0);
    }
}
