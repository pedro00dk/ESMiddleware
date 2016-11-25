package test.application;

import esm.common.RegistryManager;
import test.application.staticFibonacci.StaticFibonacci;

/**
 * @author Pedro Henrique
 */
public class ClientApplication3 {

    public static void main(String[] args) {

        System.out.println("Creating a registry proxy with the Registry AOR");
        RegistryManager.initialize(RegistryApplication.REGISTRY_ABSOLUTE_OBJECT_REFERENCE);

        System.out.println("Searching in the registry for StaticFibonacci services");
        StaticFibonacci staticFibonacci = (StaticFibonacci) RegistryManager.lookup("StaticFibonacci");

        // Application

        int waitTimeMilliseconds = 1;
        boolean showResult = true;

        long distributedApplicationStartingTime = System.currentTimeMillis();

        for (int i = 0; i < 200; i++) {
            int result = staticFibonacci.f(40);
            if (showResult) {
                System.out.println("Fib of " + 40 + " = " + result);
            }
            try {
                Thread.sleep(waitTimeMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - distributedApplicationStartingTime);
    }
}
