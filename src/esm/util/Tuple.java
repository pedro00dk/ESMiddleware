package esm.util;

import java.io.Serializable;

/**
 * The tuple holds two generics elements.
 *
 * @param <T> the type of the first element.
 * @param <U> the type of the second element.
 * @author Pedro Henrique
 */
public class Tuple<T, U> implements Serializable {

    /**
     * The first element.
     */
    private T e1;

    /**
     * The second element.
     */
    private U e2;

    /**
     * Creates the tuple with the received elements.
     *
     * @param e1 the first element
     * @param e2 the second element
     */
    public Tuple(T e1, U e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Returns the first element.
     *
     * @return the first element
     */
    public T getE1() {
        return e1;
    }

    /**
     * Returns the second element.
     *
     * @return the secont element
     */
    public U getE2() {
        return e2;
    }
}
