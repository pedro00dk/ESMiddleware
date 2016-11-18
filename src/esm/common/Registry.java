package esm.common;

import esm.distribution.instance.RemoteObject;
import esm.distribution.instance.StaticRemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * The Registry is the main common service that offers invocation transparency between the client and server. Is used to
 * store {@link RemoteObject}s {@link esm.distribution.invocation.Proxy}s by the server, and this objects can be
 * get by the clients.
 *
 * @author Pedro Henrique
 */
interface Registry extends StaticRemoteObject {

    @Override
    default String getIdentifier() {
        return "Registry";
    }

    /**
     * Bind a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} to this Registry.
     *
     * @param remoteObject the object to bind
     * @throws IllegalArgumentException if the received remote object is not a proxy
     */
    void bind(RemoteObject remoteObject) throws IllegalArgumentException;

    /**
     * Bind a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} to this Registry, if is already
     * bound, rebind the remote object, if the type is different will throws an {@link IllegalArgumentException}.
     *
     * @param remoteObject the object to unbind
     * @throws IllegalArgumentException if the received remote object is not a proxy
     */
    void rebind(RemoteObject remoteObject) throws IllegalArgumentException;

    /**
     * Unbind a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} to this Registry, this method uses
     * only the remote object {@link AbsoluteObjectReference} to remove from this
     * Registry.
     *
     * @param remoteObject the object to unbind
     * @throws IllegalArgumentException if the received remote object is not a proxy
     */
    void unbind(RemoteObject remoteObject) throws IllegalArgumentException;

    /**
     * Gets a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} from the Registry.
     *
     * @param remoteObjectIdentifier the string type identifier of the remote object
     * @return the first found remote object
     * @throws NoSuchElementException if no element was found
     */
    RemoteObject lookup(String remoteObjectIdentifier) throws NoSuchElementException;

    /**
     * Gets all {@link RemoteObject}s {@link esm.distribution.invocation.Proxy} from the Registry.
     *
     * @param remoteObjectIdentifier the string type identifier of the remote object
     * @return the found remote objects
     */
    ArrayList<RemoteObject> lookupAll(String remoteObjectIdentifier);

    /**
     * Gets an array with all remote objects bound in this registry.
     *
     * @return a remote object array
     */
    RemoteObject[] list();
}
