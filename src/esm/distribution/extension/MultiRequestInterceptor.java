package esm.distribution.extension;

import esm.common.RegistryManager;
import esm.distribution.extension.exception.MultiRequestorMaxAttemptsReachedException;
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
public class MultiRequestInterceptor implements InvocationInterceptor<MethodInvocation, MethodResult> {

    /**
     * The identifier of the remote object.
     */
    private String remoteObjectIdentifier;

    /**
     * The number of attempts to try another invocations in different objects.
     */
    private int numberOfAttempts;

    /**
     * Creates the interceptor with the received parameters.
     *
     * @param remoteObjectIdentifier the identifier of the remote object
     * @param numberOfAttempts       the number of attempts to try another invocations in different objects
     */
    public MultiRequestInterceptor(String remoteObjectIdentifier, int numberOfAttempts) {
        this.remoteObjectIdentifier = Objects.requireNonNull(remoteObjectIdentifier, "The remote object identifier " +
                "can not be null.");
        if (numberOfAttempts < 1 || numberOfAttempts > 100) {
            throw new IllegalArgumentException("The number of attempts should be between 1 and 100");
        }
        this.numberOfAttempts = numberOfAttempts;
    }

    @Override
    public MethodResult intercept(ExcFunction<MethodInvocation, MethodResult> intercepted,
                                  MethodInvocation argument) throws Exception {
        if (numberOfAttempts == 1) {
            return intercepted.apply(argument);
        }
        ArrayList<AbsoluteObjectReference> absoluteObjectReferences = new ArrayList<>();
        for (int i = 0; i < numberOfAttempts; i++) {
            try {
                MethodResult methodResult = intercepted.apply(argument);
                if (methodResult.getRemoteMiddlewareException() != null) {
                    if (methodResult.getRemoteMiddlewareException() instanceof NoSuchMethodException
                            || methodResult.getRemoteMiddlewareException() instanceof IllegalAccessException) {
                        // Reflection fails because proxy implementation contains errors
                        return methodResult;
                    }
                    throw methodResult.getRemoteMiddlewareException();
                }
                return methodResult;
            } catch (Exception e) {
                if (i == 0) {
                    ArrayList<RemoteObject> remoteObjects = RegistryManager.lookupAll(remoteObjectIdentifier);
                    for (RemoteObject remoteObject : remoteObjects) {
                        absoluteObjectReferences.add(remoteObject.getAbsoluteObjectReference());
                    }
                }
                if (argument.getAbsoluteObjectReferences().containsAll(absoluteObjectReferences)) {
                    throw new MultiRequestorMaxAttemptsReachedException("No success in " + (i + 1) + " attempts, no " +
                            "more RemoteObject options to try a connection.");
                } else {
                    for (AbsoluteObjectReference absoluteObjectReference : absoluteObjectReferences) {
                        if (!argument.getAbsoluteObjectReferences().contains(absoluteObjectReference)) {
                            argument.getAbsoluteObjectReferences().add(absoluteObjectReference);
                        }
                    }
                }
            }
        }
        throw new MultiRequestorMaxAttemptsReachedException("The maximum number of attempts reached without an " +
                "invocation success.");
    }
}
