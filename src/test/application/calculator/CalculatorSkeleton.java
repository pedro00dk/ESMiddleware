package test.application.calculator;

import esm.distribution.invocation.Skeleton;
import esm.distribution.invocation.AbsoluteObjectReference;

/**
 * @author Pedro Henrique
 */
public class CalculatorSkeleton extends Skeleton implements Calculator {

    private int memory;

    public CalculatorSkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
        memory = 0;
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
