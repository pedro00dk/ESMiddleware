package esm.distribution.instance;

/**
 * This interface is and agreement for the implementation of static remote objects.
 * <p>
 * By default the remote objects are static, it means that all remote method invocations are called from the same
 * object, that also means that the implementations should be thread-safe.
 * <p>
 * The implementation only requires the conditions described in the {@link RemoteObject} interface.
 *
 * @author Pedro Henrique
 * @see RemoteObject
 */
public interface StaticRemoteObject extends RemoteObject {
}
