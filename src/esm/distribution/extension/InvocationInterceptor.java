package esm.distribution.extension;

import java.util.function.Function;

/**
 * This interface defines the basic method to create method invocation interceptors. This interceptors does not
 * intercept an invocation in the runtime, it should be created and managed in compile time.
 *
 * @author Pedro Henrique
 */
public interface InvocationInterceptor<T, U> {

    /**
     * Intercepts the the method invocation. The intercepted method can be called or not in this method.
     *
     * @param intercepted the intercepted method
     * @param argument    the arguments that would be used in the intercepted method
     * @return a result of the same type of the result of the intercepted method
     */
    U intercept(Function<T, U> intercepted, T argument);
}
