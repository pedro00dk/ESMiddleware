package test.application.staticFibonacci;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Skeleton;

/**
 * @author Pedro Henrique
 */
public class StaticFibonacciSkeleton extends Skeleton implements StaticFibonacci {

    public StaticFibonacciSkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
    }

    @Override
    public Integer f(Integer x) {
        if (x == 0 || x == 1) {
            return x;
        } else {
            return f(x - 1) + f(x - 2);
        }
    }
}
