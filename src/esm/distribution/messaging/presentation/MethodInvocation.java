package esm.distribution.messaging.presentation;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.util.Tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The MethodInvocation is used to invoke a method in a remote object, contains the method name, if expect result,
 * arguments and the {@link AbsoluteObjectReference} of the remote object. The invocation can be called from a secondary
 * object, but this should be accessible using the instanceGetterMethodName and instanceGetterMethodArguments from the
 * primary remote object.
 *
 * @author Pedro Henrique
 * @see esm.distribution.invocation.Skeleton
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
     * Indicates if the method invocation requires a different instance where the method should be called, the
     * instanceGetterMethodName and instanceGetterMethodArguments are called in the remote object to get the execution
     * object.
     */
    private boolean requireIndependentInstance;

    /**
     * The instance getter method name.
     */
    private String instanceGetterMethodName;

    /**
     * The instance getter method arguments, composed by an array of {@link Tuple} with the argument in the first tuple
     * element and the argument type in the second tuple element.
     */
    private Tuple<Object, Class>[] instanceGetterMethodArguments;

    /**
     * If the method returns something.
     */
    private boolean expectResult;

    /**
     * The references to the remote objects where the method will be called or already was called, the last element in
     * this list is the AbsoluteObjectReference of the remote object where the method will be called, the other elements
     * are checking elements, address where a location forwarder forwards the invocation to another remote object.
     */
    private ArrayList<AbsoluteObjectReference> absoluteObjectReferences;

    /**
     * Create the method invocation with the received arguments. The
     *
     * @param methodName                    the method name
     * @param methodArguments               the method arguments
     * @param requireIndependentInstance    if the method should be called in a secondary object
     * @param instanceGetterMethodName      the method name to access the secondary object from the primary
     * @param instanceGetterMethodArguments the instance getter method name arguments
     * @param expectResult                  if the method has result
     * @param absoluteObjectReference       the reference to the remote object
     */
    public MethodInvocation(String methodName, Tuple<Object, Class>[] methodArguments, boolean requireIndependentInstance,
                            String instanceGetterMethodName, Tuple<Object, Class>[] instanceGetterMethodArguments,
                            boolean expectResult, AbsoluteObjectReference absoluteObjectReference) {
        checkMethodNameAndArguments(methodName, methodArguments);
        this.methodName = methodName;
        this.methodArguments = methodArguments;
        if (requireIndependentInstance) {
            checkMethodNameAndArguments(instanceGetterMethodName, instanceGetterMethodArguments);
        }
        this.requireIndependentInstance = requireIndependentInstance;
        this.instanceGetterMethodName = requireIndependentInstance ? instanceGetterMethodName : null;
        this.instanceGetterMethodArguments = requireIndependentInstance ? instanceGetterMethodArguments : null;
        this.expectResult = expectResult;
        absoluteObjectReferences = new ArrayList<>();
        absoluteObjectReferences.add(
                Objects.requireNonNull(absoluteObjectReference, "The absolute object reference can not be null.")
        );
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
     * Returns if this method invocation should be called in a secondary remote object.
     *
     * @return if the method should be called in a secondary object
     */
    public boolean isRequireIndependentInstance() {
        return requireIndependentInstance;
    }

    /**
     * Returns the ar of the remote method to obtains the execution instance.
     *
     * @return the name of the method to get the execution instance
     */
    public String getInstanceGetterMethodName() {
        return instanceGetterMethodName;
    }

    /**
     * Returns the {@link Tuple} array of method arguments and types of the remote method to obtains the execution
     * instance.
     *
     * @return the method arguments to get the execution instance
     */
    public Tuple<Object, Class>[] getInstanceGetterMethodArguments() {
        return instanceGetterMethodArguments;
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
     * Returns the {@link AbsoluteObjectReference}s of the remote object. The last in the current absolute object
     * reference.
     *
     * @return the absolute object reference
     */
    public ArrayList<AbsoluteObjectReference> getAbsoluteObjectReferences() {
        return absoluteObjectReferences;
    }

    /**
     * Returns the last {@link AbsoluteObjectReference} of the remote object.
     *
     * @return the absolute object reference
     */
    public AbsoluteObjectReference getAbsoluteObjectReference() {
        return absoluteObjectReferences.get(absoluteObjectReferences.size() - 1);
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
