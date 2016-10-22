package esm.infrastructure.impl.tcp;

import esm.infrastructure.ServerRequestConnector;
import esm.infrastructure.ServerRequestHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * TCP implementation of the {@link ServerRequestConnector} interface.
 *
 * @author Pedro Henrique
 */
public class TCPServerRequestConnector implements ServerRequestConnector {

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
    private ServerSocket welcomeSocket;

    /**
     * Creates the TCPServerRequestConnector with the received port.
     *
     * @param localAddress the server local address
     * @param localPort    the server local port
     * @throws IOException if an I/O exception of some sort has occurred
     */
    public TCPServerRequestConnector(InetAddress localAddress, int localPort) throws IOException {
        this.localAddress = localAddress;
        this.localPort = localPort;
        welcomeSocket = new ServerSocket(localPort, 50, localAddress);
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
        return new TCPServerRequestHandler(welcomeSocket.accept());
    }

    @Override
    public void setTimeout(int timeout) throws SocketException {
        welcomeSocket.setSoTimeout(timeout);
    }

    @Override
    public void close() throws IOException {
        welcomeSocket.close();

    }
}
