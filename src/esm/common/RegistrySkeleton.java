package esm.common;

import esm.distribution.instance.DistributedObject;
import esm.distribution.invocation.Proxy;
import esm.distribution.invocation.Skeleton;
import esm.distribution.invocation.AbsoluteObjectReference;

import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The Registry {@link Skeleton} implementation, contains methods to bind, unbind and get {@link DistributedObject}s.
 *
 * @author Pedro Henrique
 */
public final class RegistrySkeleton extends Skeleton implements Registry {

    /**
     * The list with bound distributed objects.
     */
    private Map<AbsoluteObjectReference, DistributedObject> boundDistributedObjects;

    /**
     * As the other distributed objects, the registry needs an {@link AbsoluteObjectReference} with the address of this
     * object.
     *
     * @param absoluteObjectReference the registry absolute object reference
     */
    public RegistrySkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
        boundDistributedObjects = new Hashtable<>();
    }

    @Override
    public void bind(DistributedObject distributedObject) throws IllegalArgumentException {
        Objects.requireNonNull(distributedObject, "The distributed object can not be null.");
        if (!(distributedObject instanceof Proxy)) {
            throw new IllegalArgumentException("The distributed object should extends Proxy.");
        } else if (boundDistributedObjects.containsKey(distributedObject.getAbsoluteObjectReference())) {
            throw new IllegalArgumentException("A distributed object with the same absolute reference.");
        }
        boundDistributedObjects.put(distributedObject.getAbsoluteObjectReference(), distributedObject);
    }

    @Override
    public void rebind(DistributedObject distributedObject) throws IllegalArgumentException {
        Objects.requireNonNull(distributedObject, "The distributed object can not be null.");
        if (!(distributedObject instanceof Proxy)) {
            throw new IllegalArgumentException("The distributed object should extends Proxy.");
        }
        DistributedObject boundDistributedObject
                = boundDistributedObjects.get(distributedObject.getAbsoluteObjectReference());
        if (boundDistributedObject != null
                && !boundDistributedObject.getIdentifier().equals(distributedObject.getIdentifier())) {
            throw new IllegalArgumentException("The current bound distributed object has different type than the" +
                    "received distributed object type.");
        }
        boundDistributedObjects.put(distributedObject.getAbsoluteObjectReference(), distributedObject);
    }

    @Override
    public void unbind(DistributedObject distributedObject) throws IllegalArgumentException {
        Objects.requireNonNull(distributedObject, "The distributed object can not be null.");
        if (!(distributedObject instanceof Proxy)) {
            throw new IllegalArgumentException("The distributed object should extends Proxy.");
        }
        DistributedObject boundDistributedObjectToRemove
                = boundDistributedObjects.get(distributedObject.getAbsoluteObjectReference());
        if (boundDistributedObjectToRemove != null) {
            boundDistributedObjects.remove(boundDistributedObjectToRemove.getAbsoluteObjectReference());
        } else {
            throw new NoSuchElementException("The distributed object was not found.");
        }
    }

    @Override
    public DistributedObject lookup(String distributedObjectIdentifier)
            throws NoSuchElementException {
        Objects.requireNonNull(distributedObjectIdentifier, "The distributed object identifier can not be null.");
        DistributedObject boundDistributedObject = null;
        for (DistributedObject distributedObject : boundDistributedObjects.values()) {
            if (distributedObject.getIdentifier().equals(distributedObjectIdentifier)) {
                boundDistributedObject = distributedObject;
                break;
            }
        }
        if (boundDistributedObject != null) {
            return boundDistributedObject;
        } else {
            throw new NoSuchElementException("A distributed object with the received identifier was not found.");
        }
    }

    @Override
    public DistributedObject[] list() {
        return boundDistributedObjects.values().toArray(new DistributedObject[]{});
    }
}
