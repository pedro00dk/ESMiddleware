package test.application;

import esm.common.RegistrySkeleton;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.management.Invoker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Pedro Henrique
 */
public class RegistryApplication {

    static AbsoluteObjectReference REGISTRY_ABSOLUTE_OBJECT_REFERENCE;

    static {
        try {
            REGISTRY_ABSOLUTE_OBJECT_REFERENCE = new AbsoluteObjectReference(0, InetAddress.getLocalHost(), 49000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Creating Registry skeleton and  binding in the Invoker");
        RegistrySkeleton registrySkeleton = new RegistrySkeleton(REGISTRY_ABSOLUTE_OBJECT_REFERENCE);
        Invoker.getInstance().bind(registrySkeleton);

        System.out.println("Starting Invoker co-routines");
        Invoker.getInstance().start();

        System.out.println("Press the enter key to stop and close the application");
        try {
            int hell = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Invoker.getInstance().stop();
        }
        System.out.println("Invoker stopped, closing application");
    }
}
