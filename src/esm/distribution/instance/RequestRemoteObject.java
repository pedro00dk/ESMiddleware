package esm.distribution.instance;

/**
 * This interface is and agreement for the implementation of request remote objects.
 * <p>
 * The RequestRemoteObject provides a way to call each invocation in a different object.
 * <p>
 * The implementation requires the conditions described in the {@link RemoteObject} interface and the implementation
 * of a new method to manage the object creation.
 * <p>
 * The {@link #createInstance()} method should be implemented in the {@link esm.distribution.invocation.Skeleton}
 * implementation and throws an {@link UnsupportedOperationException} in the {@link esm.distribution.invocation.Proxy}
 * implementation  when called externally.
 *
 * @author Pedro Henrique
 * @see RemoteObject
 */
public interface RequestRemoteObject extends RemoteObject {

    /**
     * Creates a unique remote object instance that will be destroyed after used.
     *
     * @return a new remote object instance
     */
    RemoteObject createInstance();
}
