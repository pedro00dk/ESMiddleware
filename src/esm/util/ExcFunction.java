package esm.util;

/**
 * Represents a function that accepts one argument and produces a result, the function can throws a {@link Exception}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author Pedro Henrique
 */
@FunctionalInterface
public interface ExcFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws Exception a possible exception that was throw in this function
     */
    R apply(T t) throws Exception;
}
