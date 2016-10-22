package esm.distribution.messaging.presentation;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Skeleton;
import esm.util.Tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * The MethodInvocation is used to invoke a method in a distributed object, contains the method name, if expect result,
 * arguments and the {@link AbsoluteObjectReference} of the distributed object.
 *
 * @author Pedro Henrique
 * @see Skeleton
 * @see esm.distribution.invocation.Proxy
 * @see AbsoluteObjectReference
 * @see MethodResult
 */
public class MethodInvocation implements Serializable {

    /**
     * The presentation protocol name.
     */
    private final String magic = "phPresentationProtocol";

    /**
     * The method name.
     */
    private String methodName;

    /**
     * The method arguments, composed by an array of {@link Tuple} with the argument in the first tuple element and the
     * argument type in the second tuple element.
     */
    private Tuple<Object, Class>[] methodArguments;

    /**
     * If the method returns something.
     */
    private boolean expectResult;

    /**
     * The reference to the distributed object where the method will be called.
     */
    private AbsoluteObjectReference absoluteObjectReference;

    /**
     * Create the method messaging with the received arguments.
     *
     * @param methodName              the method name
     * @param expectResult            if the method has result
     * @param methodArguments         the method arguments
     * @param absoluteObjectReference the reference to the distributed object
     */
    public MethodInvocation(String methodName, boolean expectResult, Tuple<Object, Class>[] methodArguments,
                            AbsoluteObjectReference absoluteObjectReference) {
        checkMethodNameAndArguments(methodName, methodArguments);
        this.methodName = methodName;
        this.methodArguments = methodArguments;
        this.expectResult = expectResult;
        this.absoluteObjectReference
                = Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.");
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
     * Returns the {@link Tuple} array of method arguments and types of the method to be called.
     *
     * @return the method arguments
     */
    public Tuple<Object, Class>[] getMethodArguments() {
        return methodArguments;
    }

    /**
     * Returns if the method expects a result.
     *
     * @return if expect result
     */
    public boolean isExpectResult() {
        return expectResult;
    }

    /**
     * Returns the {@link AbsoluteObjectReference} of the distributed object.
     *
     * @return the absolute object reference
     */
    public AbsoluteObjectReference getAbsoluteObjectReference() {
        return absoluteObjectReference;
    }

    /**
     * Checks if the method name and arguments are valid.
     *
     * @param methodName      the method name
     * @param methodArguments the method arguments
     */
    private void checkMethodNameAndArguments(String methodName, Tuple<Object, Class>[] methodArguments) {
        Objects.requireNonNull(methodName, "The method name can not be null.");
        Objects.requireNonNull(methodArguments, "The method arguments can not be null.");
        for (Tuple<Object, Class> methodArgument : methodArguments) {
            Objects.requireNonNull(methodArgument.getE2(), "The method arguments can not contains null class types.");
        }
    }
}
