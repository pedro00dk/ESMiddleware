package test.application.requestCalculator;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Skeleton;

/**
 * @author Pedro Henrique
 */
public class RequestCalculatorSkeleton extends Skeleton implements RequestCalculator {

    private int memory;

    public RequestCalculatorSkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
        memory = 0;
    }

    @Override
    public RemoteObject createInstance() {
        return new RequestCalculatorSkeleton(getAbsoluteObjectReference());

    }

    @Override
    public synchronized Integer getMem() {
        return memory;
    }

    @Override
    public synchronized void setMem(Integer a) {
        memory = a;
    }

    @Override
    public synchronized Integer sum(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public synchronized Integer sub(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public synchronized Integer mul(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public synchronized Integer div(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public synchronized Integer mod(Integer a, Integer b) {
        return a % b;
    }

    @Override
    public void exception() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Test remote exception throw.");
    }
}
