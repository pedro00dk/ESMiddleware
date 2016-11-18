package esm.distribution.management;

import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.distribution.messaging.session.Message;
import esm.distribution.serialization.Crypto;
import esm.distribution.serialization.Marshaller;
import esm.infrastructure.ClientRequestHandler;
import esm.infrastructure.impl.tcp.TCPClientRequestHandler;

import java.io.IOException;
import java.util.Objects;

/**
 * The Requestor gets the {@link MethodInvocation}s and makes a message with it, this message are serialized and sent
 * to a {@link Invoker} in a server.
 *
 * @author Pedro Henrique
 * @see MethodInvocation
 * @see MethodResult
 * @see Invoker
 */
public class Requestor {

    /**
     * The {@link ClientRequestHandler} used in the communication.
     */
    private ClientRequestHandler clientRequestHandler;

    /**
     * Creates the Requestor instance.
     */
    public Requestor() {
    }

    /**
     * Send a {@link MethodInvocation} to the {@link Invoker} in a server.
     *
     * @param methodInvocation the method invocation, can not be null
     * @return the {@link MethodResult}, or null if a result is not expected
     * @throws IOException if the connection fails, reset or was refused
     */
    public MethodResult sendRemoteMethodInvocation(MethodInvocation methodInvocation) throws IOException {
        Objects.requireNonNull(methodInvocation, "The method invocation can not be null.");
        clientRequestHandler = new TCPClientRequestHandler(
                methodInvocation.getAbsoluteObjectReference().getServerAddress(),
                methodInvocation.getAbsoluteObjectReference().getServerPort()
        );
        clientRequestHandler.connect();
        Message requestMessage = new Message(methodInvocation);
        clientRequestHandler.send(Crypto.encrypt(Marshaller.marshall(requestMessage)));
        if (methodInvocation.isExpectResult()) {
            Message replyMessage = (Message) Marshaller.unmarshall(Crypto.decrypt(clientRequestHandler.receive()));
            return (MethodResult) replyMessage.getBody();
        } else {
            clientRequestHandler.disconnect();
            return null;
        }
    }
}
