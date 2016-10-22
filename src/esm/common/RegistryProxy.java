package esm.common;

import esm.distribution.instance.DistributedObject;
import esm.distribution.invocation.Proxy;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.util.Tuple;

import java.util.NoSuchElementException;

/**
 * The registry proxy, the method implementations invoke remotely the {@link RegistrySkeleton}.
 *
 * @author Pedro Henrique
 */
public final class RegistryProxy extends Proxy implements Registry {

    /**
     * As the other distributed objects, the registry needs an {@link AbsoluteObjectReference} with the address of the
     * distributed object.
     *
     * @param registryInvokerAbsoluteObjectReference the registry absolute object reference
     */
    public RegistryProxy(AbsoluteObjectReference registryInvokerAbsoluteObjectReference) {
        super(registryInvokerAbsoluteObjectReference);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(DistributedObject distributedObject) throws IllegalArgumentException {
        try {
            invokeRemotely("bind", true,
                    new Tuple[]{new Tuple<>(distributedObject, DistributedObject.class)}
            );
        } catch (Throwable throwable) {
            if (throwable instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) throwable;
            }
            throwable.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void rebind(DistributedObject distributedObject) throws IllegalArgumentException {
        try {
            invokeRemotely("rebind", true,
                    new Tuple[]{new Tuple<>(distributedObject, DistributedObject.class)}
            );
        } catch (Throwable throwable) {
            if (throwable instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) throwable;
            }
            throwable.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unbind(DistributedObject distributedObject) throws IllegalArgumentException {
        try {
            invokeRemotely("unbind", true,
                    new Tuple[]{new Tuple<>(distributedObject, DistributedObject.class)}
            );
        } catch (Throwable throwable) {
            if (throwable instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) throwable;
            }
            throwable.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public DistributedObject lookup(String distributedObjectIdentifier) throws NoSuchElementException {
        try {
            return (DistributedObject) invokeRemotely("lookup", true,
                    new Tuple[]{new Tuple<>(distributedObjectIdentifier, String.class)}
            );
        } catch (Throwable throwable) {
            if (throwable instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) throwable;
            }
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DistributedObject[] list() {
        try {
            return (DistributedObject[]) invokeRemotely("list", true,
                    new Tuple[]{}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
