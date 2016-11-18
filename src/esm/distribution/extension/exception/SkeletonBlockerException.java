package esm.distribution.extension.exception;

/**
 * This exception indicates that a {@link esm.distribution.extension.SkeletonBlockerInterceptor} has blocked the
 * connection.
 *
 * @author Pedro Henrique
 */
public class SkeletonBlockerException extends Exception {

    /**
     * Creates the exception with no message.
     */
    public SkeletonBlockerException() {
    }

    /**
     * Creates the exception with the received message.
     *
     * @param message the exception message
     */
    public SkeletonBlockerException(String message) {
        super(message);
    }
}
