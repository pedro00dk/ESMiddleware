package esm.infrastructure;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Defines the basic methods to implement the {@link ClientRequestHandler} interface. The ClientRequestHandler
 * communicates with a {@link ServerRequestHandler}. The ClientRequestHandler should be created with the
 * {@link ServerRequestConnector} information.
 *
 * @author Pedro Henrique
 * @see ServerRequestHandler
 * @see ServerRequestConnector
 */
public interface ClientRequestHandler {

    /**
     * Returns the address of the {@link ServerRequestConnector}.
     *
     * @return the address of the server
     */
    InetAddress getServerAddress();

    /**
     * Returns the port of the {@link ServerRequestConnector}.
     *
     * @return the port of the server
     */
    int getServerPort();

    /**
     * Connects with a {@link ServerRequestHandler} using a {@link ServerRequestConnector} to establish the connection.
     * Just can be called if this ClientRequestHandler is disconnected and not connected before.
     *
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void connect() throws IOException;

    /**
     * Sends the data to the connected {@link ServerRequestHandler}. Just can be called if this ClientRequestHandler is
     * connected and if the this method was not called before. This method can not be called two times.
     *
     * @param data the bytes to send
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void send(byte[] data) throws IOException;

    /**
     * Receives bytes from a {@link ServerRequestHandler}. Just can be called if this ClientRequestHandler is connected
     * and send method was called before. This method can not be called two times.
     *
     * @return the received bytes
     * @throws IOException if an I/O exception of some sort has occurred
     */
    byte[] receive() throws IOException;

    /**
     * Disconnects from the {@link ServerRequestHandler}. Just can be called if this ClientRequestHandler is connected.
     * This method can not be called two times.
     *
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void disconnect() throws IOException;
}
