package esm.infrastructure;

import esm.infrastructure.impl.tcp.TCPClientRequestHandler;
import esm.infrastructure.impl.tcp.TCPServerRequestConnector;
import esm.infrastructure.impl.udp.UDPClientRequestHandler;
import esm.infrastructure.impl.udp.UDPServerRequestConnector;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Creates and returns {@link ClientRequestHandler}s and {@link ServerRequestConnector}s protocol with the received
 * properties. The {@link ServerRequestHandler}s are only created when a {@link ServerRequestConnector} receives a
 * connection request, is created using the same protocol type of the connector.
 *
 * @author Pedro Henrique
 */
public final class TransportFactory {

    /**
     * Prevents instantiation.
     */
    private TransportFactory() {
    }

    /**
     * Returns a new {@link ClientRequestHandler} implemented with the received transport protocol and the
     * {@link ServerRequestConnector} {@link InetAddress} and port.
     *
     * @param transportProtocol the transport protocol
     * @param serverAddress     the server address
     * @param serverPort        the server port
     * @return the ClientRequestHandler instance
     */
    public static ClientRequestHandler newClientRequestHandler(TransportProtocol transportProtocol,
                                                               InetAddress serverAddress, int serverPort)
            throws IOException {
        switch (transportProtocol) {
            case TCP:
                return new TCPClientRequestHandler(serverAddress, serverPort);
            case UDP:
                return new UDPClientRequestHandler(serverAddress, serverPort);
        }
        return null;
    }

    /**
     * Returns a new {@link ServerRequestConnector} implemented with the received transport protocol listening requests
     * in the received {@link InetAddress} and port.
     *
     * @param transportProtocol the transport protocol
     * @param localPort         the local port
     * @return the ClientRequestHandler instance
     */
    public static ServerRequestConnector newServerRequestConnector(TransportProtocol transportProtocol,
                                                                   InetAddress localAddress, int localPort)
            throws IOException {
        switch (transportProtocol) {
            case TCP:
                return new TCPServerRequestConnector(localAddress, localPort);
            case UDP:
                return new UDPServerRequestConnector(localAddress, localPort);
        }
        return null;
    }

    /**
     * The protocols that can be used to create request handlers and connectors.
     */
    public enum TransportProtocol {
        TCP, UDP
    }

}
