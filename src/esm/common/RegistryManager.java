package esm.common;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.management.Invoker;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class offers the {@link Registry} services of the {@link RegistryProxy} and {@link RegistrySkeleton}
 * implementations. This class lets the middleware internal classes have access to the Registry services.
 *
 * @author Pedro Henrique
 */
public final class RegistryManager {

    private static RegistryProxy registryProxy;

    /**
     * The internal registry skeleton.
     */
    private static RegistrySkeleton registrySkeleton;

    /**
     * Prevents instantiation.
     */
    private RegistryManager() {
    }

    // Proxy methods

    /**
     * Initializes the proxy services of the Registry.
     *
     * @param absoluteObjectReference the absolute object reference of the remote registry
     */
    public static void initialize(AbsoluteObjectReference absoluteObjectReference) {
        registryProxy = new RegistryProxy(absoluteObjectReference);
        registryProxy.checkConnection();
    }

    /**
     * Bind a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} to this Registry.
     *
     * @param remoteObject the object to bind
     * @throws IllegalArgumentException if the received remote object is not a proxy
     */
    public static void bind(RemoteObject remoteObject) throws IllegalArgumentException {
        Objects.requireNonNull(registryProxy, "The Registry poxy was not initialized.");
        registryProxy.bind(remoteObject);
    }

    /**
     * Bind a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} to this Registry, if is already
     * bound, rebind the remote object, if the type is different will throws an {@link IllegalArgumentException}.
     *
     * @param remoteObject the object to unbind
     * @throws IllegalArgumentException if the received remote object is not a proxy
     */
    public static void rebind(RemoteObject remoteObject) throws IllegalArgumentException {
        Objects.requireNonNull(registryProxy, "The Registry poxy was not initialized.");
        registryProxy.rebind(remoteObject);
    }

    /**
     * Unbind a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} to this Registry, this method uses
     * only the remote object {@link AbsoluteObjectReference} to remove from this
     * Registry.
     *
     * @param remoteObject the object to unbind
     * @throws IllegalArgumentException if the received remote object is not a proxy
     */
    public static void unbind(RemoteObject remoteObject) throws IllegalArgumentException {
        Objects.requireNonNull(registryProxy, "The Registry poxy was not initialized.");
        registryProxy.unbind(remoteObject);
    }

    /**
     * Gets a {@link RemoteObject} {@link esm.distribution.invocation.Proxy} from the Registry.
     *
     * @param remoteObjectIdentifier the string type identifier of the remote object
     * @return the first found remote object
     * @throws NoSuchElementException if no element was found
     */
    public static RemoteObject lookup(String remoteObjectIdentifier) throws NoSuchElementException {
        Objects.requireNonNull(registryProxy, "The Registry poxy was not initialized.");
        return registryProxy.lookup(remoteObjectIdentifier);
    }

    /**
     * Gets an array with all remote objects bound in this registry.
     *
     * @return a remote object array
     */
    public static RemoteObject[] list() {
        Objects.requireNonNull(registryProxy, "The Registry poxy was not initialized.");
        return registryProxy.list();
    }

    // Skeleton methods

    /**
     * Creates the Registry skeleton and binds in the {@link Invoker}, the Invoker should be stopped. To start the
     * registry, just start the Invoker.
     *
     * @param absoluteObjectReference the absolute object reference of the Registry, the absolute object reference
     *                                address should be local
     */
    public static void createRegistryInInvoker(AbsoluteObjectReference absoluteObjectReference) {
        Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        if (!absoluteObjectReference.getServerAddress().isAnyLocalAddress()) {
            //throw new IllegalArgumentException("The absolute object reference server address should be local.");
        }
        if (registrySkeleton != null) {
            throw new IllegalStateException("The Registry is already bound.");
        }
        registrySkeleton = new RegistrySkeleton(absoluteObjectReference);
        Invoker.getInstance().bind(registrySkeleton);
    }

    /**
     * Destroys the Registry unbinding from the {@link Invoker} first. the Invoker should be stopped.
     */
    public static void destroyRegistryFromInvoker() {
        if (registrySkeleton == null) {
            throw new IllegalStateException("The Registry is already unbound.");
        }
        Invoker.getInstance().unbind(registrySkeleton);
        registrySkeleton = null;
    }
}
