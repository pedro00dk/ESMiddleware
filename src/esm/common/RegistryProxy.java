package esm.common;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.util.Tuple;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * The registry proxy, the method implementations invoke remotely the {@link RegistrySkeleton}.
 *
 * @author Pedro Henrique
 */
public final class RegistryProxy extends Proxy implements Registry {

    /**
     * As the other remote objects, the registry needs an {@link AbsoluteObjectReference} with the address of the
     * remote object.
     *
     * @param registryInvokerAbsoluteObjectReference the registry absolute object reference
     */
    public RegistryProxy(AbsoluteObjectReference registryInvokerAbsoluteObjectReference) {
        super(registryInvokerAbsoluteObjectReference);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bind(RemoteObject remoteObject) throws IllegalArgumentException {
        try {
            invokeRemotely(
                    "bind", new Tuple[]{new Tuple<>(remoteObject, RemoteObject.class)},
                    false, null, null,
                    true
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
    public void rebind(RemoteObject remoteObject) throws IllegalArgumentException {
        try {
            invokeRemotely(
                    "rebind", new Tuple[]{new Tuple<>(remoteObject, RemoteObject.class)},
                    false, null, null,
                    true
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
    public void unbind(RemoteObject remoteObject) throws IllegalArgumentException {
        try {
            invokeRemotely(
                    "unbind", new Tuple[]{new Tuple<>(remoteObject, RemoteObject.class)},
                    false, null, null,
                    true
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
    public RemoteObject lookup(String remoteObjectIdentifier) throws NoSuchElementException {
        try {
            return (RemoteObject) invokeRemotely(
                    "lookup", new Tuple[]{new Tuple<>(remoteObjectIdentifier, String.class)},
                    false, null, null,
                    true
            );
        } catch (Throwable throwable) {
            if (throwable instanceof NoSuchElementException) {
                throw (NoSuchElementException) throwable;
            }
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<RemoteObject> lookupAll(String remoteObjectIdentifier) {
        try {
            return (ArrayList<RemoteObject>) invokeRemotely(
                    "lookupAll", new Tuple[]{new Tuple<>(remoteObjectIdentifier, String.class)},
                    false, null, null,
                    true
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RemoteObject[] list() {
        try {
            return (RemoteObject[]) invokeRemotely(
                    "list", new Tuple[]{},
                    false, null, null,
                    true
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
