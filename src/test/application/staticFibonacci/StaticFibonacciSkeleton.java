package test.application.staticFibonacci;

import esm.distribution.extension.SkeletonOptions;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Skeleton;

/**
 * @author Pedro Henrique
 */
public class StaticFibonacciSkeleton extends Skeleton implements StaticFibonacci {

    public StaticFibonacciSkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
    }

    public StaticFibonacciSkeleton(AbsoluteObjectReference absoluteObjectReference, SkeletonOptions skeletonOptions) {
        super(absoluteObjectReference, skeletonOptions);
    }

    @Override
    public Integer f(Integer x) {
        //System.out.println("f called");
        return fib(x);
    }

    @Override
    public Integer[] f(Integer[] x) {
        //System.out.println("f[] called");
        Integer[] results = new Integer[x.length];
        for (int i = 0; i < x.length; i++) {
            results[i] = fib(x[i]);
        }
        return results;
    }

    private Integer fib(Integer x) {
        if (x == 0 || x == 1) {
            return x;
        } else {
            return f(x - 1) + f(x - 2);
        }
    }
}
