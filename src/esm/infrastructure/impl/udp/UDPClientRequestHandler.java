package esm.infrastructure.impl.udp;

import esm.infrastructure.ClientRequestHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

/**
 * UDP implementation of the {@link ClientRequestHandler} interface.
 *
 * @author Pedro Henrique
 */
public class UDPClientRequestHandler implements ClientRequestHandler {

    /**
     * The ServerRequestConnector address.
     */
    private InetAddress serverAddress;

    /**
     * The ServerRequestConnector port.
     */
    private int serverPort;

    /**
     * The connected socket.
     */
    private DatagramSocket connectedDatagramSocket;

    /**
     * The socket input stream.
     */
    private DataInputStream dataInputStream;

    /**
     * The socket output stream.
     */
    private DataOutputStream dataOutputStream;

    /**
     * The connection state of this ClientRequestHandler.
     */
    private boolean connected;

    /**
     * Indicates if this ClientRequestHandler already was sent bytes.
     */
    private boolean sent;

    /**
     * Indicates if this ClientRequestHandler was connected before.
     */
    private boolean connectedBefore;

    /**
     * Creates a new TCPClientRequestHandler.
     *
     * @param serverAddress the server address
     * @param serverPort    the server port
     * @throws IOException if an I/O exception of some sort has occurred
     */
    public UDPClientRequestHandler(InetAddress serverAddress, int serverPort) throws IOException {
        this.serverAddress = Objects.requireNonNull(serverAddress, "The server address can not be null.");
        if (serverPort < 0 || serverPort > 65535) {
            throw new IllegalArgumentException("The server port should be between 0 and 65535.");
        }
        this.serverPort = serverPort;
        connectedDatagramSocket = new DatagramSocket();
        connected = false;
        sent = false;
        connectedBefore = false;
    }

    @Override
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public void connect() throws IOException {
        if (connected) {
            throw new IllegalStateException("This ClientRequestHandler is already connected.");
        } else if (connectedBefore) {
            throw new IllegalStateException("This ClientRequestHandler was connected before.");
        }
        connected = true;
        connectedDatagramSocket.send(new DatagramPacket(new byte[0], 0, serverAddress, serverPort));
        DatagramPacket serverRequestHandlerConnectionInfoPacket = new DatagramPacket(new byte[0], 0);
        connectedDatagramSocket.receive(serverRequestHandlerConnectionInfoPacket);
        dataInputStream = new DataInputStream(new DatagramInputStream(
                connectedDatagramSocket,
                serverRequestHandlerConnectionInfoPacket.getAddress(),
                serverRequestHandlerConnectionInfoPacket.getPort())
        );
        dataOutputStream = new DataOutputStream(new DatagramOutputStream(
                connectedDatagramSocket,
                serverRequestHandlerConnectionInfoPacket.getAddress(),
                serverRequestHandlerConnectionInfoPacket.getPort())
        );
    }

    @Override
    public void send(byte[] data) throws IOException {
        if (!connected) {
            throw new IllegalStateException("This ClientRequestHandler is disconnected.");
        } else if (sent) {
            throw new IllegalStateException("This ClientRequestHandler already was sent data.");
        }
        Objects.requireNonNull(data, "The data to send can not be null.");
        sent = true;
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data, 0, data.length);
        dataOutputStream.flush();
    }

    @Override
    public byte[] receive() throws IOException {
        if (!connected) {
            throw new IllegalStateException("This ClientRequestHandler is disconnected.");
        } else if (!sent) {
            throw new IllegalStateException("This ClientRequestHandler was not sent data yet.");
        }
        byte[] data = new byte[dataInputStream.readInt()];
        dataInputStream.read(data, 0, data.length);
        disconnect();
        return data;
    }

    @Override
    public void disconnect() throws IOException {
        if (!connected) {
            throw new IllegalStateException("The ClientRequestHandler is already disconnected.");
        }
        connected = false;
        connectedBefore = true;
        dataOutputStream.close();
        dataInputStream.close();
        connectedDatagramSocket.close();
    }
}
