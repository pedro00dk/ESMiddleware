package esm.distribution.extension.exception;

/**
 * This exception indicates that a {@link esm.distribution.extension.MultiRequestInterceptor} was failed to send a
 * remote invocation in a maximum number of attempts received.
 *
 * @author Pedro Henrique
 */
public class MultiRequestorMaxAttemptsReachedException extends Exception {

    /**
     * Creates the exception with no message.
     */
    public MultiRequestorMaxAttemptsReachedException() {
    }

    /**
     * Creates the exception with the received message.
     *
     * @param message the exception message
     */
    public MultiRequestorMaxAttemptsReachedException(String message) {
        super(message);
    }
}
