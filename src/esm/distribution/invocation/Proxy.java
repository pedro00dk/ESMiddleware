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
     * Creates the client proxy with the received {@link AbsoluteObjectReference}.
     *
     * @param absoluteObjectReference the reference to the server skeleton object
     */
    public Proxy(AbsoluteObjectReference absoluteObjectReference) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
    }

    @Override
    public final AbsoluteObjectReference getAbsoluteObjectReference() {
        return absoluteObjectReference;
    }

    @Override
    public String checkConnection() {
        try {
            return (String) invokeRemotely(
                    "checkConnection", new Tuple[]{},
                    false, null, null,
                    true
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * Invokes remotely a method, if the remote method returns void or if the result is not expected the result of the
     * invocation is null, in the second case, the {@link Throwable}s will be ignored too. If the remote method
     * (not the middleware) throws a {@link Throwable} this method will throws too with the same message and the
     * received result will be null (the tuple).
     * The remote invocation can be done in a secondary remote object accessible by the primary remote object using the
     * instanceGetterMethodName and instanceGetterMethodArguments.
     * The void methods can expect result, it ensure the method execution and eventual exception throws can be get
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
     */
    protected final Object invokeRemotely(String methodName, Tuple<Object, Class>[] methodArguments,
                                          boolean requireIndependentInstance, String instanceGetterMethodName,
                                          Tuple<Object, Class>[] instanceGetterMethodArguments,
                                          boolean expectResult) throws Throwable {
        MethodInvocation methodInvocation = new MethodInvocation(methodName, methodArguments,
                requireIndependentInstance, instanceGetterMethodName, instanceGetterMethodArguments, expectResult,
                absoluteObjectReference);
        Requestor requestor = new Requestor();
        MethodResult methodResult = new MultiRequestInterceptor(requestor, getIdentifier(), 4)
                .intercept(requestor::sendRemoteMethodInvocation, methodInvocation);
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
