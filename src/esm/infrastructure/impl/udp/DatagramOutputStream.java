package esm.infrastructure.impl.udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

/**
 * Extends the {@link OutputStream} using a {@link DatagramSocket} and {@link DatagramPacket}s to send info to the
 * receiver DatagramSocket.
 *
 * @author Pedro Henrique
 */
class DatagramOutputStream extends OutputStream {

    /**
     * The udp socket.
     */
    private DatagramSocket connectedSocket;

    /**
     * The connected address.
     */
    private InetAddress connectedAddress;

    /**
     * The connected port.
     */
    private int connectedPort;

    /**
     * The datagram byte stream.
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * The max packet size.
     */
    private static final int MAX_DATAGRAM_SIZE = 2048;

    /**
     * Creates the OutputStream with the received socket, address and port.
     *
     * @param connectedSocket  the connected udp socket
     * @param connectedAddress the connected address
     * @param connectedPort    the connected port
     */
    public DatagramOutputStream(DatagramSocket connectedSocket, InetAddress connectedAddress, int connectedPort) {
        this.connectedSocket = connectedSocket;
        this.connectedAddress = Objects.requireNonNull(connectedAddress, "The connected address can not be null.");
        this.connectedPort = connectedPort;
        byteArrayOutputStream = new ByteArrayOutputStream(1024);
    }

    @Override
    public void write(int b) throws IOException {
        byteArrayOutputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        byteArrayOutputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byteArrayOutputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        byte[] bufferedData = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.reset();
        for (int i = 0; i < bufferedData.length; i += MAX_DATAGRAM_SIZE) {
            int packetDataLength = Math.min(MAX_DATAGRAM_SIZE, bufferedData.length - i);
            byte[] packetData = new byte[packetDataLength];
            System.arraycopy(bufferedData, 0, packetData, 0, packetDataLength);
            DatagramPacket sendPacket
                    = new DatagramPacket(packetData, packetDataLength, connectedAddress, connectedPort);
            connectedSocket.send(sendPacket);
        }
    }
}
