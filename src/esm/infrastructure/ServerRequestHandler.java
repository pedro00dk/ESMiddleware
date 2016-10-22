package esm.infrastructure;

import java.io.IOException;

/**
 * Defines the basic methods to implement the {@link ServerRequestHandler} interface. The ServerRequestHandler receives
 * requests from a {@link ClientRequestHandler}. The ServerRequestHandler is created already connected by a
 * {@link ServerRequestConnector}.
 *
 * @author Pedro Henrique
 * @see ServerRequestConnector
 * @see ClientRequestHandler
 */
public interface ServerRequestHandler {

    /**
     * Receives bytes from a {@link ClientRequestHandler}. Just can be called if this ServerRequestHandler is connected
     * and this method was not called before. This method can not be called two times.
     *
     * @return the received bytes
     * @throws IOException if an I/O exception of some sort has occurred
     */
    byte[] receive() throws IOException;

    /**
     * Sends the data to the connected {@link ClientRequestHandler}. Just can be called if this ServerRequestHandler is
     * connected and if the method received was called before. This method can not be called two times.
     *
     * @param data the bytes to send
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void send(byte[] data) throws IOException;

    /**
     * Disconnects from the {@link ClientRequestHandler}. Just can be called if this ServerRequestHandler is connected.
     * This method can not be called two times.
     *
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void disconnect() throws IOException;
}
