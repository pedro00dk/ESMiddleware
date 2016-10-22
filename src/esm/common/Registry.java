package esm.common;

import esm.distribution.instance.DistributedObject;
import esm.distribution.invocation.AbsoluteObjectReference;

import java.util.NoSuchElementException;

/**
 * The Registry is the main common service that offers invocation transparency between the client and server. Is used to
 * store {@link DistributedObject}s {@link esm.distribution.invocation.Proxy}s by the server, and this objects can be
 * get by the clients.
 *
 * @author Pedro Henrique
 */
public interface Registry extends DistributedObject {

    @Override
    default String getIdentifier() {
        return "Registry";
    }

    /**
     * Bind a {@link DistributedObject} {@link esm.distribution.invocation.Proxy} to this Registry.
     *
     * @param distributedObject the object to bind
     * @throws IllegalArgumentException if the received distributed object is not a proxy
     */
    void bind(DistributedObject distributedObject) throws IllegalArgumentException;

    /**
     * Bind a {@link DistributedObject} {@link esm.distribution.invocation.Proxy} to this Registry, if is already
     * bound, rebind the distributed object, if the type is different will throws an {@link IllegalArgumentException}.
     *
     * @param distributedObject the object to unbind
     * @throws IllegalArgumentException if the received distributed object is not a proxy
     */
    void rebind(DistributedObject distributedObject) throws IllegalArgumentException;

    /**
     * Unbind a {@link DistributedObject} {@link esm.distribution.invocation.Proxy} to this Registry, this method uses
     * only the distributed object {@link AbsoluteObjectReference} to remove from this
     * Registry.
     *
     * @param distributedObject the object to unbind
     * @throws IllegalArgumentException if the received distributed object is not a proxy
     */
    void unbind(DistributedObject distributedObject) throws IllegalArgumentException;

    /**
     * Gets a {@link DistributedObject} {@link esm.distribution.invocation.Proxy} from the Registry.
     *
     * @param distributedObjectIdentifier the string type identifier of the distributed object
     * @return the first found distributed object
     * @throws NoSuchElementException if no element was found
     */
    DistributedObject lookup(String distributedObjectIdentifier) throws NoSuchElementException;

    /**
     * Gets an array with all distributed objects bound in this registry.
     *
     * @return a distributed object array
     */
    DistributedObject[] list();
}
