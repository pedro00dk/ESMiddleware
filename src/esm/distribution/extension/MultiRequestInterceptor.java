package esm.distribution.extension;

import esm.common.RegistryManager;
import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.management.Requestor;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.ThrowableFunction;

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
     * The {@link Requestor} that will be used to send the invocations.
     */
    private Requestor requestor;

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
     * @param requestor              the requestor that will be used to send the invocations.
     * @param remoteObjectIdentifier the identifier of the remote object
     * @param numberOfAttempts       the number of attempts to try another invocations in different objects
     */
    public MultiRequestInterceptor(Requestor requestor, String remoteObjectIdentifier, int numberOfAttempts) {
        this.requestor = Objects.requireNonNull(requestor, "The Requestor can not be null.");
        this.remoteObjectIdentifier = Objects.requireNonNull(remoteObjectIdentifier, "The remote object identifier " +
                "can not be null.");
        if (numberOfAttempts < 1 || numberOfAttempts > 100) {
            throw new IllegalArgumentException("The number of attempts should be between 1 and 100");
        }
        this.numberOfAttempts = numberOfAttempts;
    }

    @Override
    public MethodResult intercept(ThrowableFunction<MethodInvocation, MethodResult> intercepted,
                                  MethodInvocation argument) throws Throwable {
        ArrayList<AbsoluteObjectReference> absoluteObjectReferences = new ArrayList<>();
        for (int i = 0; i < numberOfAttempts; i++) {
            try {
                return requestor.sendRemoteMethodInvocation(argument);
            } catch (Exception e) {
                if (i == 0) {
                    ArrayList<RemoteObject> remoteObjects = RegistryManager.lookupAll(remoteObjectIdentifier);
                    for (RemoteObject remoteObject : remoteObjects) {
                        absoluteObjectReferences.add(remoteObject.getAbsoluteObjectReference());
                    }
                }
                if (argument.getAbsoluteObjectReferences().containsAll(absoluteObjectReferences)) {
                    e.printStackTrace();
                } else {
                    for (AbsoluteObjectReference absoluteObjectReference : absoluteObjectReferences) {
                        if (!argument.getAbsoluteObjectReferences().contains(absoluteObjectReference)) {
                            argument.getAbsoluteObjectReferences().add(absoluteObjectReference);
                        }
                    }
                }
            }
        }
        throw new UnsupportedOperationException("The maximum number of attempts reached without invocation success.");
    }
}
