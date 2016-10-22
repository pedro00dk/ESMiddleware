package esm.distribution.instance;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.distribution.invocation.Skeleton;

import java.io.Serializable;

/**
 * This interface is an agreement for the implementation of distributed objects.
 * <p>
 * For all methods to be implemented, the parameters and return types should be complex (if has parameters and if does
 * not return void).
 * <p>
 * The interface should implement the method {@link #getIdentifier()} that gets the a {@link String} unique type
 * identifier and the ({@link #getAbsoluteObjectReference()}) that returns the {@link AbsoluteObjectReference} of the
 * distributed object.
 * <p>
 * The interface that extends this should be implemented two times, the first implementation is the real functional
 * class with the own behaviour methods, but this implementation needs to extend the {@link Skeleton} class, it will
 * provide support for remote invocations.
 * <p>
 * The second implementation is the class that connects with the first, for each method of the interface, the method in
 * the second implementation establishes a connection with a remote first implementation using a
 * {@link AbsoluteObjectReference} to find it, the connection is already implemented by the {@link Proxy} class, just
 * needing extends it and call the invocation method.
 *
 * @author Pedro Henrique
 * @see Proxy
 * @see Skeleton
 * @see AbsoluteObjectReference
 */
public interface DistributedObject extends Serializable {

    /**
     * Returns a simple string identifier of this object class.
     *
     * @return the object class identifier
     */
    String getIdentifier();

    /**
     * Returns the {@link AbsoluteObjectReference} of the distributed object.
     *
     * @return the absolute object reference
     */
    AbsoluteObjectReference getAbsoluteObjectReference();
}
