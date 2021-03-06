package esm.distribution.instance;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.distribution.invocation.Skeleton;

import java.io.Serializable;

/**
 * This interface is an agreement for the implementation of remote objects.
 * <p>
 * For all methods to be implemented, the parameters and return types should be complex (if has parameters and if does
 * not returns void). The methods can only throws {@link Exception}s, not {@link Throwable}s or {@link Error}s.
 * <p>
 * The interface that extends this should be implemented two times, the first implementation is the real functional
 * class with the own behaviour methods, but this implementation needs to extend the {@link Skeleton} class, it will
 * provide support for remote invocations.
 * <p>
 * The second implementation is the class that connects with the first, for each method of the interface, the method in
 * the second implementation establishes a connection with a remote first implementation using a
 * {@link AbsoluteObjectReference} to find it, the connection is already implemented by the {@link Proxy} class, just
 * needing extends it and call the remote method.
 *
 * @author Pedro Henrique
 * @see Proxy
 * @see Skeleton
 * @see AbsoluteObjectReference
 */
public interface RemoteObject extends Serializable {

    /**
     * Returns a simple string identifier of this object class.
     *
     * @return the object class identifier
     */
    String getIdentifier();

    /**
     * Returns the {@link AbsoluteObjectReference} of the remote object.
     *
     * @return the absolute object reference
     */
    AbsoluteObjectReference getAbsoluteObjectReference();

    /**
     * Checks the connection between the {@link Proxy} and the {@link Skeleton}.
     */
    String checkConnection();
}
