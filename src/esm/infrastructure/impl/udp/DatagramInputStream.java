package esm.infrastructure.impl.udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

/**
 * Extends the {@link InputStream} using a {@link DatagramSocket} and {@link DatagramPacket}s to receive info from the
 * sender DatagramSocket.
 *
 * @author Pedro Henrique
 */
class DatagramInputStream extends InputStream {

    /**
     * The connected udp socket.
     */
    private DatagramSocket connectedSocket;

    /**
     * The expected connected socket address.
     */
    private InetAddress expectedConnectedAddress;

    /**
     * The expected connected socket port.
     */
    private int expectedConnectedPort;

    /**
     * The datagram byte stream.
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * The max packet size.
     */
    private static final int MAX_DATAGRAM_SIZE = 2048;

    /**
     * Creates the DatagramInputStream with the received udp socket. This constructor limits the receives only of the
     * socket of the received address and port.
     *
     * @param connectedSocket          the connected udp socket
     * @param expectedConnectedAddress the expected connected address of the data sender socket, if null received from
     *                                 any address
     * @param expectedConnectedPort    the expected connected port of the data sender socket, if <= 0 receives from any
     *                                 port
     */
    public DatagramInputStream(DatagramSocket connectedSocket, InetAddress expectedConnectedAddress, int expectedConnectedPort) {
        this.connectedSocket = connectedSocket;
        this.expectedConnectedAddress
                = Objects.requireNonNull(expectedConnectedAddress, "The expected address can not be null.");
        this.expectedConnectedPort = expectedConnectedPort;
        byteArrayOutputStream = new ByteArrayOutputStream(1024);
    }

    @Override
    public int read() throws IOException {
        readToEnsureSize(1);
        byte[] readByte = new byte[1];
        extractFromBuffer(readByte, 0, 1);
        return readByte[0];
    }

    @Override
    public int read(byte[] b) throws IOException {
        readToEnsureSize(b.length);
        extractFromBuffer(b, 0, b.length);
        return b.length;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        readToEnsureSize(len);
        extractFromBuffer(b, off, len);
        return len;
    }

    @Override
    public long skip(long n) throws IOException {
        readToEnsureSize((int) n);
        read(new byte[(int) n]);
        return n;
    }

    private void readToEnsureSize(int size) throws IOException {
        while (byteArrayOutputStream.size() < size) {
            DatagramPacket receivedPacket = new DatagramPacket(new byte[MAX_DATAGRAM_SIZE], MAX_DATAGRAM_SIZE);
            connectedSocket.receive(receivedPacket);
            if ((expectedConnectedAddress == null || expectedConnectedAddress.equals(receivedPacket.getAddress()))
                    && expectedConnectedPort <= 0 || expectedConnectedPort == receivedPacket.getPort()) {
                byteArrayOutputStream.write(receivedPacket.getData(), 0, receivedPacket.getLength());
            }
        }
    }

    private void extractFromBuffer(byte[] buffer, int offset, int length) throws IOException {
        if (buffer.length < offset + length) {
            throw new IllegalArgumentException("Illegal buffer size.");
        }
        byte[] bytesOfStream = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.reset();
        System.arraycopy(bytesOfStream, 0, buffer, offset, length);
        int overlapedLength = bytesOfStream.length - length;
        if (overlapedLength > 0) {
            byteArrayOutputStream.write(bytesOfStream, length, overlapedLength);
        }
    }
}
