package esm.infrastructure.impl.tcp;

import esm.infrastructure.ClientRequestHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

/**
 * TCP implementation of the {@link ClientRequestHandler} interface.
 *
 * @author Pedro Henrique
 */
public class TCPClientRequestHandler implements ClientRequestHandler {

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
    private Socket connectedSocket;

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
     */
    public TCPClientRequestHandler(InetAddress serverAddress, int serverPort) {
        this.serverAddress = Objects.requireNonNull(serverAddress, "The server address can not be null.");
        if (serverPort < 0 || serverPort > 65535) {
            throw new IllegalArgumentException("The server port should be between 0 and 65535.");
        }
        this.serverPort = serverPort;
        connectedSocket = new Socket();
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
        connectedSocket.connect(new InetSocketAddress(serverAddress, serverPort));
        dataInputStream = new DataInputStream(connectedSocket.getInputStream());
        dataOutputStream = new DataOutputStream(connectedSocket.getOutputStream());
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
        connectedSocket.close();
    }
}
