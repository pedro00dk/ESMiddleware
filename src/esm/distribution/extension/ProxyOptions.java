package esm.distribution.extension;

import java.io.Serializable;

/**
 * The ProxyOptions sets a conjunct of properties for {@link esm.distribution.invocation.Proxy}s connections.
 *
 * @author Pedro Henrique
 */
public class ProxyOptions implements Serializable {

    /**
     * If the connection fails, try to reconnect with the registered remote object in the Proxy.
     */
    private boolean retryConnectionOnRegistered;

    /**
     * The number of attempts to try reconnect with the registered remote object in the Proxy.
     */
    private int attemptsToTryOnRegistered;

    /**
     * The delay time to try reconnect on the registered remote object in milliseconds. Is added with itself each try.
     */
    private long delayTimeToConnectOnRegistered;

    /**
     * If the connection fails, try to reconnect with a different remote object.
     */
    private boolean retryConnectionOnOther;

    /**
     * The number of attempts to try a connection with different remote objects, each try is done in a different remote
     * object.
     */
    private int attemptsToTryOnOther;

    /**
     * If the connection in the registered remote object fails but was done in other remote object, the
     * {@link esm.distribution.invocation.AbsoluteObjectReference} of the Proxy will be updated to the new remote
     * object.
     */
    private boolean updateAbsoluteObjectReference;

    /**
     * Creates a default ProxyOptions, not allowing any type of reconnection.
     */
    public ProxyOptions() {
        retryConnectionOnRegistered = false;
        attemptsToTryOnRegistered = 0;
        delayTimeToConnectOnRegistered = 0;
        retryConnectionOnOther = false;
        attemptsToTryOnOther = 0;
        updateAbsoluteObjectReference = false;
    }

    /**
     * Creates the ProxyOptions with the received properties.
     *
     * @param retryConnectionOnRegistered    retry connection if the first fails on the registered remote object
     * @param attemptsToTryOnRegistered      the number of attempts to retry a connection if the first fails on the
     *                                       registered remote object
     * @param delayTimeToConnectOnRegistered the delay to try a reconnection on the registered object, is added with
     *                                       itself each connection retry in milliseconds
     * @param retryConnectionOnOther         retry connection if all reconnection tries fails on the registered remote
     *                                       object in other remote objects
     * @param attemptsToTryOnOther           the number of attempts to retry a connection on other remote objects
     * @param updateAbsoluteObjectReference  if the connection in the registered remote object fails but was done in
     *                                       other remote object, tha absolute object reference will be updated to the
     *                                       remote objects where the connection was successful
     */
    public ProxyOptions(boolean retryConnectionOnRegistered, int attemptsToTryOnRegistered,
                        long delayTimeToConnectOnRegistered, boolean retryConnectionOnOther, int attemptsToTryOnOther,
                        boolean updateAbsoluteObjectReference) {
        this.retryConnectionOnRegistered = retryConnectionOnRegistered;
        if (retryConnectionOnRegistered && (attemptsToTryOnRegistered < 1 || attemptsToTryOnRegistered > 100)) {
            throw new IllegalArgumentException("The number of attempts on the same should be between 1 and 100.");
        }
        this.attemptsToTryOnRegistered = retryConnectionOnRegistered ? attemptsToTryOnRegistered : 0;
        if (retryConnectionOnRegistered
                && (delayTimeToConnectOnRegistered < 0 || delayTimeToConnectOnRegistered > 100)) {
            throw new IllegalArgumentException("The delay time to retry connections should be between 0 and 100.");
        }
        this.delayTimeToConnectOnRegistered = retryConnectionOnRegistered ? delayTimeToConnectOnRegistered : 0;
        this.retryConnectionOnOther = retryConnectionOnOther;
        if (retryConnectionOnOther && (attemptsToTryOnOther < 1 || attemptsToTryOnOther > 100)) {
            throw new IllegalArgumentException("The number of attempts on the other should be between 1 and 100.");
        }
        this.attemptsToTryOnOther = retryConnectionOnOther ? attemptsToTryOnOther : 0;
        this.updateAbsoluteObjectReference = updateAbsoluteObjectReference;
    }

    /**
     * Returns if the proxy should retry a connection if the first fails in the registered remote object.
     *
     * @return if the proxy should retry a connection in the registered remote object
     */
    public boolean isRetryConnectionOnRegistered() {
        return retryConnectionOnRegistered;
    }

    /**
     * Gets the number of attempts to try reconnect with the registered remote object in the Proxy.
     *
     * @return the number of attempts to try reconnect with the registered remote object
     */
    public int getAttemptsToTryOnRegistered() {
        return attemptsToTryOnRegistered;
    }

    /**
     * Gets the delay time to retry the connection with the registered remote object in milliseconds.
     *
     * @return the time to retry a connection
     */
    public long getDelayTimeToConnectOnRegistered() {
        return delayTimeToConnectOnRegistered;
    }

    /**
     * Returns if the proxy should retry a connection with other remote objects if all connection tries fails with the
     * registered remote object.
     *
     * @return if the proxy should retry a connection with other remote objects
     */
    public boolean isRetryConnectionOnOther() {
        return retryConnectionOnOther;
    }

    /**
     * Gets the number of attempts to try connect with other remote objects in the Proxy.
     *
     * @return the number of attempts to try reconnect with other remote objects
     */
    public int getAttemptsToTryOnOther() {
        return attemptsToTryOnOther;
    }

    /**
     * Returns if the Proxy should update the {@link esm.distribution.invocation.AbsoluteObjectReference} if the
     * connection in the registered remote object fails but was done in other remote object.
     *
     * @return if the Pxoxy should update the registered remote object
     */
    public boolean isUpdateAbsoluteObjectReference() {
        return updateAbsoluteObjectReference;
    }
}
