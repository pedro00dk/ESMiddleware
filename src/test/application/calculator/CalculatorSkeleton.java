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
    public Integer getMem() {
        return memory;
    }

    @Override
    public void setMem(Integer a) {
        memory = a;
    }

    @Override
    public Integer sum(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        return a % b;
    }

    @Override
    public void exception() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Test remote exception throw.");
    }
}
