package test.application;

import esm.common.RegistryManager;
import esm.distribution.extension.ProxyOptions;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.management.Invoker;
import test.application.staticFibonacci.StaticFibonacciProxy;
import test.application.staticFibonacci.StaticFibonacciSkeleton;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Pedro Henrique
 */
public class ServerApplication2 {

    private static AbsoluteObjectReference FIBONACCI_ABSOLUTE_OBJECT_REFERENCE;

    static {
        try {
            FIBONACCI_ABSOLUTE_OBJECT_REFERENCE = new AbsoluteObjectReference(133, InetAddress.getLocalHost(), 50001);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("Creating the StaticFibonacci Skeleton and Proxy");
        StaticFibonacciSkeleton fibonacciSkeleton = new StaticFibonacciSkeleton(FIBONACCI_ABSOLUTE_OBJECT_REFERENCE);
        StaticFibonacciProxy fibonacciProxy = new StaticFibonacciProxy(
                FIBONACCI_ABSOLUTE_OBJECT_REFERENCE,
                new ProxyOptions(false, 0, 0, true, 3, true)
        );

        System.out.println("Binding the UnicastCalculator skeleton in the Invoker");
        Invoker invoker = Invoker.getInstance();
        invoker.bind(fibonacciSkeleton);

        System.out.println("Starting the Invoker co-routines");
        invoker.start();

        System.out.println("Binding the UnicastCalculator proxy in the Registry");
        RegistryManager.initialize(RegistryApplication.REGISTRY_ABSOLUTE_OBJECT_REFERENCE);
        RegistryManager.bind(fibonacciProxy);

        System.out.println("Press the enter key to unbindFromInvoker and close the application");
        try {
            int byteCount = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            RegistryManager.unbind(fibonacciProxy);
            Invoker.getInstance().stop();
        }
        System.out.println("UnicastCalculator proxy unbound from the Registry and Invoker stopped, closing application");
    }
}
