package test.application.staticFibonacci;

import esm.distribution.instance.StaticRemoteObject;

/**
 * @author Pedro Henrique
 */
public interface StaticFibonacci extends StaticRemoteObject {

    @Override
    default String getIdentifier() {
        return "StaticFibonacci";
    }

    Integer f(Integer x);
}
