package esm.distribution.extension;

import esm.common.RegistryManager;
import esm.distribution.extension.exception.SkeletonBlockerException;
import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.management.Requestor;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.ExcFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This interceptor checks the Skeleton options to block or forward remote method invocations.
 *
 * @author Pedro Henrique
 */
public class SkeletonBlockerInterceptor extends QOSObserverInterceptor {

    /**
     * The identifier of the remote object.
     */
    private String remoteObjectIdentifier;

    /**
     * The skeleton blocking options.
     */
    private SkeletonOptions skeletonOptions;

    /**
     * Creates the interceptor with the received parameters.
     *
     * @param remoteObjectIdentifier the identifier of the remote object
     * @param skeletonOptions        the skeleton connection options
     */
    public SkeletonBlockerInterceptor(String remoteObjectIdentifier, SkeletonOptions skeletonOptions) {
        this.remoteObjectIdentifier
                = Objects.requireNonNull(remoteObjectIdentifier, "The remote object identifier can not be null.");
        this.skeletonOptions = Objects.requireNonNull(skeletonOptions, "The Skeleton options can not be null.");
    }

    @Override
    public MethodResult intercept(ExcFunction<MethodInvocation, MethodResult> intercepted, MethodInvocation argument) {

        System.out.println(argument.getMethodName() + " called, current: " + getCurrentInvocationCount() +
                ", total: " + getInvocationCount());

        if (getCurrentInvocationCount() < skeletonOptions.getMaxConnections()) {
            try {
                return super.intercept(intercepted, argument);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error();
            }
        }

        switch (skeletonOptions.getBlockMode()) {
            case BLOCK_MAX_CONN:
                System.out.println(argument.getMethodName() + " call blocked");
                return new MethodResult(argument.getMethodName(), null, null,
                        new SkeletonBlockerException("Connection blocked, max concurrent connections reached."),
                        argument.getAbsoluteObjectReference()
                );
            case FORWARD_MAX_CON:
                System.out.println(argument.getMethodName() + " call forwarded");
                ArrayList<AbsoluteObjectReference> absoluteObjectReferences = new ArrayList<>();
                ArrayList<RemoteObject> remoteObjects = RegistryManager.lookupAll(remoteObjectIdentifier);
                for (RemoteObject remoteObject : remoteObjects) {
                    absoluteObjectReferences.add(remoteObject.getAbsoluteObjectReference());
                }
                int currentAORListSize = argument.getAbsoluteObjectReferences().size();
                for (AbsoluteObjectReference absoluteObjectReference : absoluteObjectReferences) {
                    if (!argument.getAbsoluteObjectReferences().contains(absoluteObjectReference)) {
                        argument.getAbsoluteObjectReferences().add(absoluteObjectReference);
                        break;
                    }
                }
                if (argument.getAbsoluteObjectReferences().size() == currentAORListSize) {
                    return new MethodResult(argument.getMethodName(), null, null,
                            new SkeletonBlockerException("Failed to forward, no remote objects of the same type " +
                                    "available."),
                            argument.getAbsoluteObjectReference());
                }
                try {
                    return new Requestor().sendRemoteMethodInvocation(argument);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Error();
                }
        }
        return null;
    }
}
