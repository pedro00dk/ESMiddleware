package esm.distribution.invocation;

import esm.distribution.messaging.presentation.MethodInvocation;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

/**
 * The Absolute Object Reference is used to invoke a remote object, contains a object id, a server {@link InetAddress}
 * and port.
 *
 * @author Pedro Henrique
 * @see Skeleton
 * @see Proxy
 * @see MethodInvocation
 */
public class AbsoluteObjectReference implements Serializable {

    /**
     * The object id.
     */
    private int objectId;

    /**
     * The server {@link InetAddress}.
     */
    private InetAddress serverAddress;

    /**
     * The server port.
     */
    private int serverPort;

    /**
     * Creates the absolute object reference with the data to access the distributed a object.
     *
     * @param objectId      the object id
     * @param serverAddress the server address
     * @param serverPort    the server port
     */
    public AbsoluteObjectReference(int objectId, InetAddress serverAddress, int serverPort) {
        this.objectId = objectId;
        this.serverAddress = Objects.requireNonNull(serverAddress, "The server address can not be null.");
        if (serverPort < 0 || serverPort > 65535) {
            throw new IllegalArgumentException("The server port should be between 0 and 65535.");
        }
        this.serverPort = serverPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbsoluteObjectReference that = (AbsoluteObjectReference) o;
        return objectId == that.objectId && serverPort == that.serverPort && serverAddress.equals(that.serverAddress);
    }

    @Override
    public int hashCode() {
        int result = objectId;
        result = 31 * result + serverAddress.hashCode();
        result = 31 * result + serverPort;
        return result;
    }

    /**
     * Returns the object id of this {@link AbsoluteObjectReference}.
     *
     * @return the object id
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Returns the server {@link InetAddress} of this {@link AbsoluteObjectReference}.
     *
     * @return the server address
     */
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    /**
     * Returns the server port of this {@link AbsoluteObjectReference}.
     *
     * @return the server port
     */
    public int getServerPort() {
        return serverPort;
    }
}
