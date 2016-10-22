package esm.infrastructure.impl.tcp;

import esm.infrastructure.ServerRequestHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * TCP implementation of the {@link ServerRequestHandler} interface.
 *
 * @author Pedro Henrique
 */
public class TCPServerRequestHandler implements ServerRequestHandler {

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
     * The connection state of this ServerRequestHandler.
     */
    private boolean connected;

    /**
     * Indicates if this ServerRequestHandler already was received bytes.
     */
    private boolean received;

    /**
     * Creates a new TCPServerRequestHandler with the connected socket.
     *
     * @param connectedSocket the connected socket.
     * @throws IOException if an I/O exception of some sort has occurred
     */
    TCPServerRequestHandler(Socket connectedSocket) throws IOException {
        this.connectedSocket = Objects.requireNonNull(connectedSocket, "The connected socket can not be null.");
        dataInputStream = new DataInputStream(connectedSocket.getInputStream());
        dataOutputStream = new DataOutputStream(connectedSocket.getOutputStream());
        connected = true;
        received = false;
    }

    @Override
    public byte[] receive() throws IOException {
        if (!connected) {
            throw new IllegalStateException("This ServerRequestHandler is disconnected.");
        } else if (received) {
            throw new IllegalStateException("This ServerRequestHandler already was received data.");
        }
        received = true;
        byte[] data = new byte[dataInputStream.readInt()];
        dataInputStream.read(data, 0, data.length);
        return data;
    }

    @Override
    public void send(byte[] data) throws IOException {
        if (!connected) {
            throw new IllegalStateException("This ServerRequestHandler is disconnected.");
        } else if (!received) {
            throw new IllegalStateException("This ServerRequestHandler was not received data yet.");
        }
        Objects.requireNonNull(data, "The data to send can not be null.");
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data, 0, data.length);
        dataOutputStream.flush();
        disconnect();
    }

    @Override
    public void disconnect() throws IOException {
        if (!connected) {
            throw new IllegalStateException("The ServerRequestHandler is already disconnected.");
        }
        connected = false;
        dataOutputStream.close();
        dataInputStream.close();
        connectedSocket.close();
    }
}
