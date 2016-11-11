package esm.distribution.messaging.presentation;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.util.Tuple;

import java.io.Serializable;

/**
 * The method result is used to hold the result and a possible {@link Throwable} of a remote method messaging.
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
     * The {@link Throwable} throw in the remote method, null if nothing was throw.
     */
    private Throwable throwable;

    /**
     * Creates a new method result with the remote method messaging result and possibles exceptions.
     *
     * @param methodName the method name
     * @param result     the remote method messaging result and result type in a tuple
     * @param throwable  the possible throwable throw in the remote method
     */
    public MethodResult(String methodName, Tuple<Object, Class> result, Throwable throwable) {
        this.methodName = methodName;
        this.result = result;
        this.throwable = throwable;
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
     * Returns the {@link Throwable} throw in the remote method, null if nothing was throw.
     *
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }
}
