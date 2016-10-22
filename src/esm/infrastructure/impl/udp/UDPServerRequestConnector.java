package esm.infrastructure.impl.udp;

import esm.infrastructure.ServerRequestConnector;
import esm.infrastructure.ServerRequestHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * UDP implementation of the {@link ServerRequestConnector} interface.
 *
 * @author Pedro Henrique
 */
public class UDPServerRequestConnector implements ServerRequestConnector {

    /**
     * The server local address.
     */
    private InetAddress localAddress;

    /**
     * The server local port.
     */
    private int localPort;

    /**
     * The server socket.
     */
    private DatagramSocket welcomeDatagramSocket;

    /**
     * Creates the UDPServerRequestConnector with the received port.
     *
     * @param localAddress the server local address
     * @param localPort    the server local port
     * @throws IOException if an I/O exception of some sort has occurred
     */
    public UDPServerRequestConnector(InetAddress localAddress, int localPort) throws IOException {
        this.localAddress = localAddress;
        this.localPort = localPort;
        welcomeDatagramSocket = new DatagramSocket(localPort, localAddress);
    }

    @Override
    public InetAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public int getLocalPort() {
        return localPort;
    }

    @Override
    public ServerRequestHandler accept() throws IOException {
        DatagramPacket clientRequestHandlerConnectionInfoPacket = new DatagramPacket(new byte[0], 0);
        welcomeDatagramSocket.receive(clientRequestHandlerConnectionInfoPacket);
        DatagramSocket connectedDatagramSocket = new DatagramSocket();
        connectedDatagramSocket.send(new DatagramPacket(new byte[0], 0,
                clientRequestHandlerConnectionInfoPacket.getAddress(),
                clientRequestHandlerConnectionInfoPacket.getPort())
        );
        ServerRequestHandler serverRequestHandler = new UDPServerRequestHandler(
                connectedDatagramSocket, clientRequestHandlerConnectionInfoPacket.getAddress(),
                clientRequestHandlerConnectionInfoPacket.getPort()
        );
        return serverRequestHandler;
    }

    @Override
    public void setTimeout(int timeout) throws SocketException {
        welcomeDatagramSocket.setSoTimeout(timeout);
    }

    @Override
    public void close() throws IOException {
        welcomeDatagramSocket.close();
    }
}
