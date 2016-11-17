package test.application;

import esm.common.RegistryManager;
import test.application.staticFibonacci.StaticFibonacci;

/**
 * @author Pedro Henrique
 */
public class ClientApplication {

    public static void main(String[] args) {

        System.out.println("Creating a registry proxy with the Registry AOR");
        RegistryManager.initialize(RegistryApplication.REGISTRY_ABSOLUTE_OBJECT_REFERENCE);

        System.out.println("Searching in the registry for StaticFibonacci services");
        StaticFibonacci staticFibonacci = (StaticFibonacci) RegistryManager.lookup("StaticFibonacci");

        // Application

        int waitTimeMilliseconds = 1;
        boolean showResult = true;

        long distributedApplicationStartingTime = System.currentTimeMillis();

        for (int i = 0; i < 45; i++) {
            int result = staticFibonacci.f(i);
            if (showResult) {
                System.out.println("Fib of " + i + " = " + result);
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
