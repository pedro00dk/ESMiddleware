package esm.common;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.distribution.invocation.Skeleton;

import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The Registry {@link Skeleton} implementation, contains methods to bind, unbind and get {@link RemoteObject}s.
 *
 * @author Pedro Henrique
 */
public final class RegistrySkeleton extends Skeleton implements Registry {

    /**
     * The list with bound remote objects.
     */
    private Map<AbsoluteObjectReference, RemoteObject> boundRemoteObjects;

    /**
     * As the other remote objects, the registry needs an {@link AbsoluteObjectReference} with the address of this
     * object.
     *
     * @param absoluteObjectReference the registry absolute object reference
     */
    public RegistrySkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
        boundRemoteObjects = new Hashtable<>();
    }

    @Override
    public synchronized void bind(RemoteObject remoteObject) throws IllegalArgumentException {
        Objects.requireNonNull(remoteObject, "The remote object can not be null.");
        if (!(remoteObject instanceof Proxy)) {
            throw new IllegalArgumentException("The remote object should extends Proxy.");
        } else if (boundRemoteObjects.containsKey(remoteObject.getAbsoluteObjectReference())) {
            throw new IllegalArgumentException("A remote object with the same absolute reference.");
        }
        boundRemoteObjects.put(remoteObject.getAbsoluteObjectReference(), remoteObject);
    }

    @Override
    public synchronized void rebind(RemoteObject remoteObject) throws IllegalArgumentException {
        Objects.requireNonNull(remoteObject, "The remote object can not be null.");
        if (!(remoteObject instanceof Proxy)) {
            throw new IllegalArgumentException("The remote object should extends Proxy.");
        }
        RemoteObject boundRemoteObject
                = boundRemoteObjects.get(remoteObject.getAbsoluteObjectReference());
        if (boundRemoteObject != null
                && !boundRemoteObject.getIdentifier().equals(remoteObject.getIdentifier())) {
            throw new IllegalArgumentException("The current bound remote object has different type than the received " +
                    "remote object type.");
        }
        boundRemoteObjects.put(remoteObject.getAbsoluteObjectReference(), remoteObject);
    }

    @Override
    public synchronized void unbind(RemoteObject remoteObject) throws IllegalArgumentException {
        Objects.requireNonNull(remoteObject, "The remote object can not be null.");
        if (!(remoteObject instanceof Proxy)) {
            throw new IllegalArgumentException("The remote object should extends Proxy.");
        }
        RemoteObject boundRemoteObjectToRemove
                = boundRemoteObjects.get(remoteObject.getAbsoluteObjectReference());
        if (boundRemoteObjectToRemove != null) {
            boundRemoteObjects.remove(boundRemoteObjectToRemove.getAbsoluteObjectReference());
        } else {
            throw new NoSuchElementException("The remote object was not found.");
        }
    }

    @Override
    public synchronized RemoteObject lookup(String remoteObjectIdentifier)
            throws NoSuchElementException {
        Objects.requireNonNull(remoteObjectIdentifier, "The remote object identifier can not be null.");
        RemoteObject boundRemoteObject = null;
        for (RemoteObject remoteObject : boundRemoteObjects.values()) {
            if (remoteObject.getIdentifier().equals(remoteObjectIdentifier)) {
                boundRemoteObject = remoteObject;
                break;
            }
        }
        if (boundRemoteObject != null) {
            return boundRemoteObject;
        } else {
            throw new NoSuchElementException("A remote object with the received identifier was not found.");
        }
    }

    @Override
    public synchronized RemoteObject[] list() {
        return boundRemoteObjects.values().toArray(new RemoteObject[]{});
    }
}
