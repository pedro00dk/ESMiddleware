package test.application.unicastCalculator;

import esm.distribution.instance.UnicastRemoteObject;

/**
 * @author Pedro Henrique
 */
public interface UnicastCalculator extends UnicastRemoteObject {

    default String getIdentifier() {
        return "UnicastCalculator";
    }

    Integer getMem();

    void setMem(Integer a);

    Integer sum(Integer a, Integer b);

    Integer sub(Integer a, Integer b);

    Integer mul(Integer a, Integer b);

    Integer div(Integer a, Integer b);

    Integer mod(Integer a, Integer b);

    void exception() throws UnsupportedOperationException;
}
