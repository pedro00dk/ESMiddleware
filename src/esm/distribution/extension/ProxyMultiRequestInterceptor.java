package esm.distribution.extension;

import esm.common.RegistryManager;
import esm.distribution.extension.exception.ProxyMultiRequestorException;
import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.ExcFunction;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This interceptor tries to send subsequent remote method invocations if the first invocation fails, the subsequent
 * method invocations can access different remote objects with different states and variables.
 *
 * @author Pedro Henrique
 */
public class ProxyMultiRequestInterceptor implements InvocationInterceptor<MethodInvocation, MethodResult> {

    /**
     * The identifier of the remote object.
     */
    private String remoteObjectIdentifier;

    /**
     * The proxy connection options.
     */
    private ProxyOptions proxyOptions;

    /**
     * Creates the interceptor with the received parameters.
     *
     * @param remoteObjectIdentifier the identifier of the remote object
     * @param proxyOptions           the proxy connection options
     */
    public ProxyMultiRequestInterceptor(String remoteObjectIdentifier, ProxyOptions proxyOptions) {
        this.remoteObjectIdentifier
                = Objects.requireNonNull(remoteObjectIdentifier, "The remote object identifier can not be null.");
        this.proxyOptions = Objects.requireNonNull(proxyOptions, "The Proxy options can not be null.");
    }

    @Override
    public MethodResult intercept(ExcFunction<MethodInvocation, MethodResult> intercepted,
                                  MethodInvocation argument) throws Exception {
        try {
            return intercepted.apply(argument);
        } catch (Exception e) {
            if (!proxyOptions.isRetryConnectionOnRegistered() && !proxyOptions.isRetryConnectionOnOther()) {
                throw new ProxyMultiRequestorException("Failed to connect in the first try (retry connection on " +
                        "registered and on others remote objects is false).");
            }
        }
        if (proxyOptions.isRetryConnectionOnRegistered()) {
            for (int i = 0; i < proxyOptions.getAttemptsToTryOnRegistered(); i++) {
                try {
                    return intercepted.apply(argument);
                } catch (Exception e) {
                    // Delay to reconnect (not after the last connection try)
                    if (i != proxyOptions.getAttemptsToTryOnRegistered() - 1) {
                        Thread.sleep(proxyOptions.getDelayTimeToConnectOnRegistered() * (i + 1));
                    } else if (!proxyOptions.isRetryConnectionOnOther()) {
                        throw new ProxyMultiRequestorException("The maximum number of attempts reached without an " +
                                "invocation success on registered remote object " +
                                "(proxyOptions.isRetryConnectionOnOther is false).");
                    }
                }
            }
        }
        if (proxyOptions.isRetryConnectionOnOther()) {
            ArrayList<AbsoluteObjectReference> absoluteObjectReferences = new ArrayList<>();
            ArrayList<RemoteObject> remoteObjects = RegistryManager.lookupAll(remoteObjectIdentifier);
            for (RemoteObject remoteObject : remoteObjects) {
                absoluteObjectReferences.add(remoteObject.getAbsoluteObjectReference());
            }
            for (int i = 0; i < proxyOptions.getAttemptsToTryOnOther(); i++) {
                try {
                    if (argument.getAbsoluteObjectReferences().containsAll(absoluteObjectReferences)) {
                        throw new ProxyMultiRequestorException("No success in " + (i + 1) + " attempts, no more " +
                                "RemoteObject options to try a connection.");
                    } else {
                        for (AbsoluteObjectReference absoluteObjectReference : absoluteObjectReferences) {
                            if (!argument.getAbsoluteObjectReferences().contains(absoluteObjectReference)) {
                                argument.getAbsoluteObjectReferences().add(absoluteObjectReference);
                                break;
                            }
                        }
                    }
                    return intercepted.apply(argument);
                } catch (Exception e) {

                    // Do not delay to reconnect in different remote objects
                }
            }
        }
        throw new ProxyMultiRequestorException("The maximum number of attempts reached without an invocation success.");
    }
}
