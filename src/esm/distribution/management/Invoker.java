package esm.distribution.management;

import esm.distribution.SOOMConfig;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Skeleton;
import esm.distribution.messaging.presentation.MethodInvocation;
import esm.distribution.messaging.presentation.MethodResult;
import esm.distribution.messaging.session.Message;
import esm.distribution.serialization.Marshaller;
import esm.infrastructure.ServerRequestConnector;
import esm.infrastructure.ServerRequestHandler;
import esm.infrastructure.TransportFactory;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@link Invoker} manages the {@link ServerRequestConnector}s and {@link ServerRequestHandler}s, this object
 * receives messages from {@link Requestor}s and sends to the registered {@link Skeleton}s. This class is a singleton,
 * the instance can be obtained using the method {@link #getInstance()}.
 *
 * @author Pedro Henrique
 * @see Invoker
 * @see MethodInvocation
 * @see MethodResult
 */
public class Invoker {

    /**
     * The instance of this class.
     */
    private static Invoker instance;

    /**
     * Gets the instance of this class.
     *
     * @return the instance
     */
    public static Invoker getInstance() {
        return instance != null ? instance : (instance = new Invoker());
    }

    //

    /**
     * The map with the bound {@link Skeleton}s.
     */
    private Map<AbsoluteObjectReference, Skeleton> boundSkeletons;

    /**
     * The map with the {@link ServerRequestConnector}s.
     */
    private Map<Integer, ServerRequestConnector> serverRequestConnectors;

    /**
     * Indicates if this invoker is running.
     */
    private AtomicBoolean invokerRunning;

    /**
     * Creates the receiver without registered invokers.
     */
    private Invoker() {
        boundSkeletons = new Hashtable<>();
        serverRequestConnectors = new Hashtable<>();
        invokerRunning = new AtomicBoolean(false);
    }

    /**
     * Binds a {@link Skeleton} in this Invoker, when the Invoker starts (calling {@link #start()}), the skeletons will
     * be able to receive remote method invocations. The Skeleton implementations should be thread-safe, because they
     * can receive multiple invocations at the same time.
     *
     * @param skeleton the skeleton to bind in this Invoker, can not be null
     */
    public synchronized void bind(Skeleton skeleton) {
        Objects.requireNonNull(skeleton, "The skeleton can not be null.");
        if (invokerRunning.get()) {
            throw new IllegalStateException("Skeletons can not be bound while the server is running.");
        } else if (boundSkeletons.containsKey(skeleton.getAbsoluteObjectReference())) {
            throw new IllegalArgumentException("A skeleton with the same AbsoluteObjectReference was bound.");
        }
        boundSkeletons.put(skeleton.getAbsoluteObjectReference(), skeleton);
        int skeletonServerPort = skeleton.getAbsoluteObjectReference().getServerPort();
        if (!serverRequestConnectors.containsKey(skeletonServerPort)) {
            try {
                serverRequestConnectors.put(
                        skeletonServerPort,
                        TransportFactory.newServerRequestConnector(
                                SOOMConfig.TRANSPORT_PROTOCOL,
                                skeleton.getAbsoluteObjectReference().getServerAddress(),
                                skeleton.getAbsoluteObjectReference().getServerPort()

                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unbinds the received {@link Skeleton} from this Invoker.
     *
     * @param skeleton the skeleton to unbind from this Invoker, can not be null.
     */
    public synchronized void unbind(Skeleton skeleton) {
        Objects.requireNonNull(skeleton, "The skeleton can not be null.");
        if (invokerRunning.get()) {
            throw new IllegalStateException("Skeletons can not be unbound while the server is running.");
        } else if (!boundSkeletons.containsKey(skeleton.getAbsoluteObjectReference())) {
            throw new IllegalArgumentException("A skeleton with the same AbsoluteObjectReference was not found.");
        }
        boundSkeletons.remove(skeleton.getAbsoluteObjectReference());
        int unboundSkeletonServerPort = skeleton.getAbsoluteObjectReference().getServerPort();
        boolean foundSkeletonOnSheSameServerPort = false;
        for (Skeleton boundSkeleton : boundSkeletons.values()) {
            if (boundSkeleton.getAbsoluteObjectReference().getServerPort() == unboundSkeletonServerPort) {
                foundSkeletonOnSheSameServerPort = true;
                break;
            }
        }
        if (!foundSkeletonOnSheSameServerPort) {
            serverRequestConnectors.remove(unboundSkeletonServerPort);
        }
    }

    /**
     * Starts this Invoker and the {@link ServerRequestConnector} co-routines.
     */
    public synchronized void start() {
        if (invokerRunning.get()) {
            throw new IllegalStateException("The receiver is already running.");
        }
        invokerRunning.set(true);
        for (ServerRequestConnector serverRequestConnector : serverRequestConnectors.values()) {
            new Thread(new RequestManager(serverRequestConnector)).start();
        }
    }

    /**
     * Stops this receiver, in a maximum of 100 milliseconds all {@link ServerRequestConnector} co-routines will be
     * stopped, but the {@link MethodInvocation}s in execution will finish being processed.
     */
    public synchronized void stop() {
        if (!invokerRunning.get()) {
            throw new IllegalStateException("The receiver is already stopped.");
        }
        invokerRunning.set(false);
    }

    //

    /**
     * The {@link RequestManager} manages a {@link ServerRequestConnector} and creates {@link RequestProcessor}s.
     */
    private class RequestManager implements Runnable {
        private ServerRequestConnector serverRequestConnector;

        RequestManager(ServerRequestConnector serverRequestConnector) {
            this.serverRequestConnector = serverRequestConnector;
        }

        @Override
        public void run() {
            try {
                serverRequestConnector.setTimeout(100);
                while (invokerRunning.get()) {
                    try {
                        ServerRequestHandler serverRequestHandler = serverRequestConnector.accept();
                        new Thread(new RequestProcessor(serverRequestHandler)).start();
                    } catch (SocketTimeoutException e) {
                        // Do nothing
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The {@link RequestProcessor} calls the {@link Skeleton}s to process the {@link MethodInvocation}.
     */
    private class RequestProcessor implements Runnable {
        private ServerRequestHandler serverRequestHandler;

        RequestProcessor(ServerRequestHandler serverRequestHandler) {
            this.serverRequestHandler = serverRequestHandler;
        }

        @Override
        public void run() {

            /*
             * receiveRemoteMethodInvocation
             * This method is opposed to the sendRemoteMethodInvocation in the Requestor class
             */
            try {
                Message requestMessage = (Message) Marshaller.unmarshall(serverRequestHandler.receive());
                MethodInvocation methodInvocation = (MethodInvocation) requestMessage.getBody();
                Skeleton skeleton = boundSkeletons.get(methodInvocation.getAbsoluteObjectReference());
                MethodResult methodResult = skeleton.processRemoteInvocation(methodInvocation);
                if (methodInvocation.isExpectResult()) {
                    Message replyMessage = new Message(methodResult);
                    serverRequestHandler.send(Marshaller.marshall(replyMessage));
                } else {
                    serverRequestHandler.disconnect();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
