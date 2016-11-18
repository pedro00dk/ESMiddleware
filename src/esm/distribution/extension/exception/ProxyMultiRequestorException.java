package esm.distribution.extension.exception;

import esm.distribution.extension.ProxyMultiRequestInterceptor;

/**
 * This exception indicates that a {@link ProxyMultiRequestInterceptor} was failed to send a
 * remote invocation in a maximum number of attempts received.
 *
 * @author Pedro Henrique
 */
public class ProxyMultiRequestorException extends Exception {

    /**
     * Creates the exception with no message.
     */
    public ProxyMultiRequestorException() {
    }

    /**
     * Creates the exception with the received message.
     *
     * @param message the exception message
     */
    public ProxyMultiRequestorException(String message) {
        super(message);
    }
}
