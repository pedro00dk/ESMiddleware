package esm.distribution.messaging.presentation;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.util.Tuple;

import java.io.Serializable;

/**
 * The method result is used to hold the result and a possible {@link Exception} of a remote method messaging or of the
 * remote middleware.
 *
 * @author Pedro Henrique
 * @see esm.distribution.invocation.Skeleton
 * @see esm.distribution.invocation.Proxy
 * @see AbsoluteObjectReference
 * @see MethodInvocation
 */
public class MethodResult implements Serializable {

    /**
     * The presentation protocol name.
     */
    private final String magic = "phPresentationProtocol";

    /**
     * The method name.
     */
    private String methodName;

    /**
     * The {@link Tuple} of result and result type of the remote method messaging, null if the remote method was throw
     * something.
     */
    private Tuple<Object, Class> result;

    /**
     * The {@link Exception} throw in the remote method, null if nothing was throw.
     */
    private Exception remoteMethodException;

    /**
     * The {@link Exception} throw in the remote middleware, null if nothing was throw.
     */
    private Exception remoteMiddlewareException;

    /**
     * Creates a new method result with the remote method messaging result and possibles exceptions.
     *
     * @param methodName                the method name
     * @param result                    the remote method messaging result and result type in a tuple
     * @param remoteMethodException     the possible exception throw in the remote method
     * @param remoteMiddlewareException the possible exception throw in the remote middleware
     */
    public MethodResult(String methodName, Tuple<Object, Class> result, Exception remoteMethodException,
                        Exception remoteMiddlewareException) {
        this.methodName = methodName;
        this.result = result;
        this.remoteMethodException = remoteMethodException;
        this.remoteMiddlewareException = remoteMiddlewareException;
    }

    /**
     * Returns the name of the presentation protocol.
     *
     * @return the presentation protocol
     */
    public String getMagic() {
        return magic;
    }

    /**
     * Returns the method name to be called.
     *
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Returns the {@link Tuple} of the result and result type of the remote method messaging, null if the remote
     * method was throw something.
     *
     * @return the result
     */
    public Tuple<Object, Class> getResult() {
        return result;
    }

    /**
     * Returns the {@link Exception} throw in the remote method, null if nothing was throw.
     *
     * @return the remote method exception
     */
    public Exception getRemoteMethodException() {
        return remoteMethodException;
    }

    /**
     * Returns the {@link Exception} throw in the remote middleware, null if nothing was throw.
     *
     * @return the remote middleware exception
     */
    public Exception getRemoteMiddlewareException() {
        return remoteMiddlewareException;
    }
}
