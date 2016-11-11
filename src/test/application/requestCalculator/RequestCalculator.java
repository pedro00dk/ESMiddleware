package test.application.requestCalculator;

import esm.distribution.instance.RequestRemoteObject;

/**
 * @author Pedro Henrique
 */
public interface RequestCalculator extends RequestRemoteObject {

    default String getIdentifier() {
        return "RequestCalculator";
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
