package esm.distribution.invocation;

import esm.distribution.management.Requestor;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.Tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for proxy objects. This class is created with an {@link AbsoluteObjectReference} of the remote object,
 * used to invocation the remote object {@link Skeleton} methods.
 *
 * @author Pedro Henrique
 * @see Skeleton
 * @see AbsoluteObjectReference
 * @see MethodInvocation
 * @see MethodResult
 */
public abstract class Proxy implements Serializable {

    /**
     * The {@link AbsoluteObjectReference} of the remote object {@link Skeleton}.
     */
    private AbsoluteObjectReference absoluteObjectReference;

    /**
     * Creates the client proxy with the received {@link AbsoluteObjectReference}.
     *
     * @param absoluteObjectReference the reference to the server skeleton object
     */
    public Proxy(AbsoluteObjectReference absoluteObjectReference) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
    }

    /**
     * Returns the {@link AbsoluteObjectReference} of the remote object {@link Skeleton}.
     *
     * @return the AbsoluteObjectReference of the remote object
     */
    public final AbsoluteObjectReference getAbsoluteObjectReference() {
        return absoluteObjectReference;
    }

    /**
     * Invokes remotely a method, if the remote method returns void or if the result is not expected the result of the
     * invocation is null, in the second case, the {@link Throwable}s will be ignored too. If the remote method
     * (not the middleware) throws a {@link Throwable} this method will throws too with the same message and the
     * received result will be null (the tuple).
     *
     * @param methodName      the method name, can not be null
     * @param expectResult    if expect a result
     * @param methodArguments the method arguments, can not be null but or contains null class elements, the class
     *                        element should be the expected type of the method parameter.
     * @return the method result
     */
    protected final Object invokeRemotely(String methodName, boolean expectResult,
                                          Tuple<Object, Class>[] methodArguments) throws Throwable {
        MethodInvocation methodInvocation = new MethodInvocation(methodName, expectResult, methodArguments,
                absoluteObjectReference);
        Requestor requestor = new Requestor();
        MethodResult methodResult = requestor.sendRemoteMethodInvocation(methodInvocation);
        if (expectResult) {
            if (methodResult.getThrowable() != null) {
                throw methodResult.getThrowable();
            }
            return methodResult.getResult().getE1();
        } else {
            return null;
        }
    }
}
