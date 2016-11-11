package test.application;

import esm.common.RegistryProxy;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.management.Invoker;
import test.application.requestCalculator.RequestCalculatorProxy;
import test.application.requestCalculator.RequestCalculatorSkeleton;
import test.application.staticCalculator.StaticCalculatorProxy;
import test.application.staticCalculator.StaticCalculatorSkeleton;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Pedro Henrique
 */
public class ServerApplication {

    private static AbsoluteObjectReference CALCULATOR_ABSOLUTE_OBJECT_REFERENCE;

    static {
        try {
            CALCULATOR_ABSOLUTE_OBJECT_REFERENCE = new AbsoluteObjectReference(133, InetAddress.getLocalHost(), 50000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Creating the RequestCalculator Skeleton and Proxy");
        RequestCalculatorSkeleton calculatorSkeleton = new RequestCalculatorSkeleton(CALCULATOR_ABSOLUTE_OBJECT_REFERENCE);
        RequestCalculatorProxy calculatorProxy = new RequestCalculatorProxy(CALCULATOR_ABSOLUTE_OBJECT_REFERENCE);

        System.out.println("Binding the RequestCalculator skeleton in the Invoker");
        Invoker invoker = Invoker.getInstance();
        invoker.bind(calculatorSkeleton);

        System.out.println("Starting the Invoker co-routines");
        invoker.start();

        System.out.println("Binding the RequestCalculator proxy in the Registry");
        RegistryProxy registryProxy = new RegistryProxy(RegistryApplication.REGISTRY_ABSOLUTE_OBJECT_REFERENCE);
        registryProxy.bind(calculatorProxy);

        System.out.println("Press the enter key to stop and close the application");
        try {
            int byteCount = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            registryProxy.unbind(calculatorProxy);
            Invoker.getInstance().stop();
        }
        System.out.println("RequestCalculator proxy unbound from the Registry and Invoker stopped, closing application");
    }
}
