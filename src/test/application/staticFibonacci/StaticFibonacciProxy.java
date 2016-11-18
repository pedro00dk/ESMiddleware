package test.application.staticFibonacci;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.util.Tuple;

/**
 * @author Pedro Henrique
 */
public class StaticFibonacciProxy extends Proxy implements StaticFibonacci {

    public StaticFibonacciProxy(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
    }

    public StaticFibonacciProxy(AbsoluteObjectReference absoluteObjectReference, int numberOfAttempts) {
        super(absoluteObjectReference, numberOfAttempts);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer f(Integer x) {
        try {
            return (Integer) invokeRemotely(
                    "f", new Tuple[]{new Tuple(x, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
