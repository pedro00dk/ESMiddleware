package esm.distribution.extension;

import esm.util.ExcFunction;

/**
 * This interface defines the basic method to create method invocation interceptors. This interceptors does not
 * intercept an invocation in the runtime, it should be created and managed in compile time.
 *
 * @author Pedro Henrique
 */
public interface InvocationInterceptor<T, U> {

    /**
     * Intercepts the method invocation. The intercepted method can be called or not in this method.
     *
     * @param intercepted the intercepted method
     * @param argument    the arguments that would be used in the intercepted method
     * @return a result of the same type of the result of the intercepted method
     * @throws Exception possible exception throw by the intercepted function or by the interception operations
     */
    U intercept(ExcFunction<T, U> intercepted, T argument) throws Exception;
}
