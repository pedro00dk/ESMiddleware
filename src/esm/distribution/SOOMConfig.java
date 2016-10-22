package esm.distribution;

import esm.infrastructure.TransportFactory;

/**
 * Contains internal properties of the middleware.
 *
 * @author Pedro Henrique
 */
public class SOOMConfig {

    /**
     * The transport protocol used by the {@link esm.distribution.management.Invoker} and
     * {@link esm.distribution.management.Requestor} classes.
     */
    public static final TransportFactory.TransportProtocol TRANSPORT_PROTOCOL = TransportFactory.TransportProtocol.TCP;
}
