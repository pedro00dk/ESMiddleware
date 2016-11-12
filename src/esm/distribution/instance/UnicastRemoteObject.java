package esm.distribution.instance;

import java.io.Closeable;

/**
 * This interface is and agreement for the implementation of unicast remote objects.
 * <p>
 * The UnicastRemoteObject provides a way to separate different client invocations in different remote objects.
 * <p>
 * The implementation requires the conditions described in the {@link RemoteObject} interface. and the implementation
 * of the new methods to manage the object creation and destruction.
 * <p>
 * The {@link #createInstance()}, {@link #getInstance(Integer)} and {@link #destroyInstance(Integer)} methods should
 * be implemented in the {@link esm.distribution.invocation.Skeleton} implementation and throws an
 * {@link UnsupportedOperationException} in the {@link esm.distribution.invocation.Proxy} implementation when called
 * externally.
 * <p>
 * This interface too extends {@link Closeable} the method {@link #close()} should returns an
 * {@link UnsupportedOperationException} in the {@link esm.distribution.invocation.Skeleton} but call the method
 * {@link #destroyInstance(Integer)} in the Proxy.
 *
 * @author Pedro Henrique
 * @see RemoteObject
 */
public interface UnicastRemoteObject extends RemoteObject, Closeable {

    /**
     * Creates a unique instance of this remote object and returns a identifier to access the created instance.
     *
     * @return a identifier of the instance
     */
    Integer createInstance();

    /**
     * Returns the remote object associated with the received key.
     *
     * @param identifier the remote object identifier
     * @return a remote object
     */
    RemoteObject getInstance(Integer identifier);

    /**
     * Destroys the remote object associated with the received key.
     *
     * @param identifier the remote object identifier
     */
    void destroyInstance(Integer identifier);
}
