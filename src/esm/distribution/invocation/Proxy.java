package esm.distribution.invocation;

import esm.distribution.extension.MultiRequestInterceptor;
import esm.distribution.instance.RemoteObject;
import esm.distribution.management.Requestor;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.Tuple;

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
public abstract class Proxy implements RemoteObject {

    /**
     * The {@link AbsoluteObjectReference} of the remote object {@link Skeleton}.
     */
    private AbsoluteObjectReference absoluteObjectReference;

    /**
     * The max number of different connection tries in different remote objects.
     */
    private Integer numberOfAttempts;

    /**
     * Creates the client proxy with the received {@link AbsoluteObjectReference}.
     *
     * @param absoluteObjectReference the reference to the server skeleton object
     */
    public Proxy(AbsoluteObjectReference absoluteObjectReference) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        numberOfAttempts = 1;
    }

    /**
     * Creates the client proxy with the received {@link AbsoluteObjectReference} and the number of attempts for
     * connections, if is 1, this proxy will try to send the invocation to the remote object of the received absolute
     * object reference, if is greater than 1, it will try to connect with different remote objects.
     *
     * @param absoluteObjectReference the reference to the server skeleton object
     * @param numberOfAttempts        the number of connection attempts in different remote objects
     */
    public Proxy(AbsoluteObjectReference absoluteObjectReference, Integer numberOfAttempts) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        if (numberOfAttempts < 1 || numberOfAttempts > 100) {
            throw new IllegalArgumentException("The number of attempts should be between 1 and 100");
        }
        this.numberOfAttempts = numberOfAttempts;

    }

    @Override
    public final AbsoluteObjectReference getAbsoluteObjectReference() {
        return absoluteObjectReference;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String checkConnection() {
        try {
            return (String) invokeRemotely(
                    "checkConnection", new Tuple[]{},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error();
        }
    }

    /**
     * Invokes remotely a method, if the remote method returns void or if the result is not expected the result of the
     * invocation is null, in the second case, the {@link Exception}s will be ignored too. If the remote method
     * (not the middleware) throws an {@link Exception} this method will throws too with the same message and the
     * received result will be null (the tuple).
     * The remote invocation can be done in a secondary remote object accessible by the primary remote object using the
     * instanceGetterMethodName and instanceGetterMethodArguments.
     * The void methods can expect result, it ensures the method execution and eventual exception throws can be get
     * using expect result.
     *
     * @param methodName                    the method name, can not be null
     * @param methodArguments               the method arguments, can not be null or contains null class elements,
     *                                      the class element should be of the type of the equivalent remote method
     *                                      parameter
     * @param requireIndependentInstance    if the method should be called in a secondary object
     * @param instanceGetterMethodName      the method name to access the secondary object from the primary object, can
     *                                      not be null if requireIndependentInstance is true
     * @param instanceGetterMethodArguments the instance getter method name arguments, has the same conditions of the
     *                                      methodArguments argument, can not be null if requireIndependentInstance is
     *                                      true
     * @param expectResult                  if expect result
     * @return the method result
     * @throws Exception throws an exception that was happen in the remote method, if the middleware internally throws
     *                   an exception, it will try to recover from the problem, if is an internal {@link Error}, the
     *                   current thread will stop
     */
    protected final Object invokeRemotely(String methodName, Tuple<Object, Class>[] methodArguments,
                                          boolean requireIndependentInstance, String instanceGetterMethodName,
                                          Tuple<Object, Class>[] instanceGetterMethodArguments,
                                          boolean expectResult) throws Exception {
        MethodInvocation methodInvocation = new MethodInvocation(methodName, methodArguments,
                requireIndependentInstance, instanceGetterMethodName, instanceGetterMethodArguments, expectResult,
                absoluteObjectReference);
        MethodResult methodResult = new MultiRequestInterceptor(getIdentifier(), numberOfAttempts)
                .intercept(new Requestor()::sendRemoteMethodInvocation, methodInvocation);
        if (expectResult) {
            if (methodResult.getRemoteMiddlewareException() != null) {
                methodResult.getRemoteMiddlewareException().printStackTrace();
                throw new Error();
            }
            if (methodResult.getRemoteMethodException() != null) {
                throw methodResult.getRemoteMethodException();
            }
            return methodResult.getResult().getE1();
        } else {
            return null;
        }
    }
}
