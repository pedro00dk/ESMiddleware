package test.application.unicastCalculator;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Skeleton;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pedro Henrique
 */
public class UnicastCalculatorSkeleton extends Skeleton implements UnicastCalculator {

    private int memory;

    private AtomicInteger currentInstanceIdentifier;

    private Map<Integer, RemoteObject> createdInstances;

    public UnicastCalculatorSkeleton(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
        memory = 0;
        currentInstanceIdentifier = new AtomicInteger();
        createdInstances = new Hashtable<>();
    }

    @Override
    public Integer createInstance() {
        Integer identifier = currentInstanceIdentifier.getAndIncrement();
        createdInstances.put(identifier, new UnicastCalculatorSkeleton(getAbsoluteObjectReference()));
        return identifier;
    }

    @Override
    public RemoteObject getInstance(Integer identifier) {
        return createdInstances.get(identifier);
    }

    @Override
    public void destroyInstance(Integer identifier) {
        createdInstances.remove(identifier);
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("This operation can not be accessed by a skeleton.");
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
