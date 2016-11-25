package esm.distribution.invocation;

import esm.distribution.extension.ProxyMultiRequestInterceptor;
import esm.distribution.extension.ProxyOptions;
import esm.distribution.instance.RemoteObject;
import esm.distribution.management.Requestor;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.Tuple;

import java.util.Objects;
import java.util.function.Consumer;

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
     * The proxy connection options, the options are only used internally.
     */
    private ProxyOptions proxyOptions;

    /**
     * Creates the client proxy with the received {@link AbsoluteObjectReference}  and default {@link ProxyOptions}.
     *
     * @param absoluteObjectReference the reference to the server skeleton object
     */
    public Proxy(AbsoluteObjectReference absoluteObjectReference) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        proxyOptions = new ProxyOptions();
    }

    /**
     * Creates the client proxy with the received {@link AbsoluteObjectReference} and {@link ProxyOptions}.
     *
     * @param absoluteObjectReference the reference to the server skeleton object
     * @param proxyOptions            the proxy connection options
     */
    public Proxy(AbsoluteObjectReference absoluteObjectReference, ProxyOptions proxyOptions) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        this.proxyOptions = Objects.requireNonNull(proxyOptions, "The Proxy options can not be null.");

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
     * Returns the Proxy connection options.
     *
     * @return the Proxy options
     */
    public ProxyOptions getProxyOptions() {
        return proxyOptions;
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
        MethodResult methodResult = new ProxyMultiRequestInterceptor(getIdentifier(), proxyOptions)
                .intercept(new Requestor()::sendRemoteMethodInvocation, methodInvocation);
        if (expectResult) {
            if (methodResult.getRemoteMiddlewareException() != null) {
                methodResult.getRemoteMiddlewareException().printStackTrace();
                throw new Error();
            } else if (proxyOptions.isUpdateAbsoluteObjectReference()) {
                absoluteObjectReference = methodResult.getAbsoluteObjectReference();
            }
            if (methodResult.getRemoteMethodException() != null) {
                throw methodResult.getRemoteMethodException();
            }
            return methodResult.getResult().getE1();
        } else {
            return null;
        }
    }

    /**
     * Async invoke remotely the received method, this method calls the {@link #invokeRemotely(String, Tuple[], boolean,
     * String, Tuple[], boolean)} in a exclusive thread and the result is always expected. When the invocation finishes,
     * the result is processed by the received result consumer, if the invocation throws an exception, it should be
     * captured by the exception consumer.
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
     * @param resultConsumer                the consumer for the invocation result (can ont be null)
     * @param exceptionConsumer             the consumer for the invocation exception (can ont be null)
     */
    @SuppressWarnings("unchecked")
    protected final void asyncInvokeRemotely(String methodName, Tuple<Object, Class>[] methodArguments,
                                             boolean requireIndependentInstance, String instanceGetterMethodName,
                                             Tuple<Object, Class>[] instanceGetterMethodArguments,
                                             Consumer resultConsumer, Consumer exceptionConsumer) {
        Objects.requireNonNull(resultConsumer, "The result consumer can not be null.");
        Objects.requireNonNull(exceptionConsumer, "The exception consumer can not be null.");
        new Thread(
                () -> {
                    try {
                        Object result = invokeRemotely(
                                methodName, methodArguments, requireIndependentInstance, instanceGetterMethodName,
                                instanceGetterMethodArguments, true
                        );
                        resultConsumer.accept(result);
                    } catch (Exception e) {
                        exceptionConsumer.accept(e);
                    }
                }
        ).start();
    }
}
