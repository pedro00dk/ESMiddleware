package esm.infrastructure;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Defines the basic methods to implement the {@link ServerRequestConnector} interface. The ServerRequestConnector
 * receives connections from a {@link ClientRequestHandler}. The ServerRequestConnector creates a new
 * {@link ServerRequestHandler} that is connected with the {@link ClientRequestHandler} that was required the
 * connection. The connections are expected in a port number. The local {@link InetAddress} and port number
 * should be set in the constructor.
 *
 * @author Pedro Henrique
 * @see ServerRequestHandler
 * @see ClientRequestHandler
 */
public interface ServerRequestConnector {

    /**
     * Returns the {@link InetAddress} of this {@link ServerRequestConnector}.
     *
     * @return the address of this server
     */
    InetAddress getLocalAddress();

    /**
     * Returns the port of this {@link ServerRequestConnector}.
     *
     * @return the port of this server
     */
    int getLocalPort();

    /**
     * Waits for a connection of a {@link ClientRequestHandler} in the connection port, when receives a connection
     * request, creates a {@link ServerRequestHandler} connected with the ClientRequestHandler that was required the
     * connection.
     *
     * @return a connected {@link ServerRequestHandler}
     * @throws IOException if an I/O exception of some sort has occurred
     */
    ServerRequestHandler accept() throws IOException;

    /**
     * Sets the timeout in milliseconds of accept calls, if not receive a call in the received timeout, the method
     * {@link #accept()} will throws a {@link java.net.SocketTimeoutException}.
     *
     * @param timeout the accept timeout
     * @throws SocketException if an Socket exception of some sort has occurred
     */
    void setTimeout(int timeout) throws SocketException;

    /**
     * Closes this {@link ServerRequestConnector}, after closed, this object can not accept new connections.
     *
     * @throws IOException if an I/O exception of some sort has occurred
     */
    void close() throws IOException;
}
