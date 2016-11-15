package test.application;

import esm.common.RegistryManager;
import test.application.unicastCalculator.UnicastCalculator;

import java.util.Random;

/**
 * @author Pedro Henrique
 */
public class ClientApplication {

    public static void main(String[] args) {

        System.out.println("Creating a registry proxy with the Registry AOR");
        RegistryManager.initialize(RegistryApplication.REGISTRY_ABSOLUTE_OBJECT_REFERENCE);

        System.out.println("Searching in the registry for RequestCalculator services");
        UnicastCalculator unicastCalculator = (UnicastCalculator) RegistryManager.lookup("UnicastCalculator");

        // Application (sum aleatory values)

        int numberOfOperations = 1000;

        int waitTimeMilliseconds = 1;
        boolean showResult = true;

        Random prng = new Random();

        long distributedApplicationStartingTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfOperations; i++) {
            int randomValue1 = prng.nextInt();
            int randomValue2 = prng.nextInt();
            unicastCalculator.setMem(unicastCalculator.sum(randomValue1, randomValue2));
            int result = unicastCalculator.getMem();
            if (showResult) {
                System.out.println("Sum of " + randomValue1 + " and " + randomValue2 + " = " + result);
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
