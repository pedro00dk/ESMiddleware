package esm.distribution.invocation;

import esm.distribution.extension.SkeletonOptions;
import esm.distribution.instance.RemoteObject;
import esm.distribution.management.Invoker;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Base class for Skeleton objects. This class is created with an {@link AbsoluteObjectReference}, used to identify this
 * object in the {@link Invoker}. It receives invocations from proxies.
 *
 * @author Pedro Henrique
 * @see Proxy
 * @see AbsoluteObjectReference
 * @see MethodResult
 * @see MethodInvocation
 */
public abstract class Skeleton implements RemoteObject {

    /**
     * The absolute object reference this object.
     */
    private AbsoluteObjectReference absoluteObjectReference;

    /**
     * The skeleton blocking options, the options are only used internally by the invoker.
     */
    private SkeletonOptions skeletonOptions;

    /**
     * Creates the skeleton with the {@link AbsoluteObjectReference} and default {@link SkeletonOptions}.
     *
     * @param absoluteObjectReference the absolute object reference of this object
     */
    public Skeleton(AbsoluteObjectReference absoluteObjectReference) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        skeletonOptions = new SkeletonOptions();
    }

    /**
     * Creates the skeleton with the {@link AbsoluteObjectReference} and default {@link SkeletonOptions}.
     *
     * @param absoluteObjectReference the absolute object reference of this object
     * @param skeletonOptions         the Skeleton blocking options
     */
    public Skeleton(AbsoluteObjectReference absoluteObjectReference, SkeletonOptions skeletonOptions) {
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
        this.skeletonOptions = skeletonOptions;
    }

    @Override
    public AbsoluteObjectReference getAbsoluteObjectReference() {
        return absoluteObjectReference;
    }

    @Override
    public String checkConnection() {
        return "connected";
    }

    /**
     * Returns the Skeleton blocking options.
     *
     * @return the Skeleton options
     */
    public SkeletonOptions getSkeletonOptions() {
        return skeletonOptions;
    }

    /**
     * This method calls a method in the sub-class or super class with the received name and parameter types. If the
     * method implementation throws an {@link Exception}, it will returns a {@link MethodResult} with the
     * exception throw, otherwise will returns the {@link MethodResult} with the method result, if a reflection
     * exception is throw, a method result with the middleware exception will be returned. If the received
     * {@link MethodInvocation} {@link MethodInvocation#isExpectResult()} is false, null is returned and all
     * {@link Exception}s are ignored.
     * The secondary invocations to get execution objects are treated inside this method and the exception throws are
     * sent by the method result as the main method.
     *
     * @param methodInvocation the method invocation, can not be null
     * @return the method result
     */
    @SuppressWarnings("unchecked")
    public final MethodResult processRemoteInvocation(MethodInvocation methodInvocation) {
        Objects.requireNonNull(methodInvocation, "The method invocation can not be null.");
        Object executionInstance = this;
        if (methodInvocation.isRequireIndependentInstance()) {
            Object[] instanceGetterMethodArguments
                    = new Object[methodInvocation.getInstanceGetterMethodArguments().length];
            Class[] instanceGetterMethodArgumentTypes
                    = new Class[methodInvocation.getInstanceGetterMethodArguments().length];
            for (int i = 0; i < instanceGetterMethodArguments.length; i++) {
                instanceGetterMethodArguments[i] = methodInvocation.getInstanceGetterMethodArguments()[i].getE1();
                instanceGetterMethodArgumentTypes[i] = methodInvocation.getInstanceGetterMethodArguments()[i].getE2();
            }
            try {
                Method instanceGetterMethod = getClass().getMethod(
                        methodInvocation.getInstanceGetterMethodName(), instanceGetterMethodArgumentTypes
                );
                executionInstance = instanceGetterMethod.invoke(this, instanceGetterMethodArguments);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                return new MethodResult(methodInvocation.getMethodName(), null, null, e, getAbsoluteObjectReference());
            } catch (InvocationTargetException e) {
                return new MethodResult(methodInvocation.getMethodName(), null, (Exception) e.getCause(), null,
                        getAbsoluteObjectReference());
            }
        }
        Object[] methodArguments = new Object[methodInvocation.getMethodArguments().length];
        Class[] methodArgumentTypes = new Class[methodInvocation.getMethodArguments().length];
        for (int i = 0; i < methodArguments.length; i++) {
            methodArguments[i] = methodInvocation.getMethodArguments()[i].getE1();
            methodArgumentTypes[i] = methodInvocation.getMethodArguments()[i].getE2();
        }
        try {
            Method method = getClass().getMethod(methodInvocation.getMethodName(), methodArgumentTypes);
            Tuple<Object, Class> result = new Tuple<>(
                    method.invoke(executionInstance, methodArguments),
                    method.getReturnType()
            );
            if (methodInvocation.isExpectResult()) {
                return new MethodResult(methodInvocation.getMethodName(), result, null, null,
                        getAbsoluteObjectReference());
            } else {
                return null;
            }
        } catch (NoSuchMethodException | IllegalAccessException e) {
            if (methodInvocation.isExpectResult()) {
                return new MethodResult(methodInvocation.getMethodName(), null, null, e, getAbsoluteObjectReference());
            }
            return null;
        } catch (InvocationTargetException e) {
            if (methodInvocation.isExpectResult()) {
                return new MethodResult(methodInvocation.getMethodName(), null, (Exception) e.getCause(), null,
                        getAbsoluteObjectReference());
            }
            return null;
        }
    }
}
