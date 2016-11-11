package test.application.staticCalculator;

import esm.distribution.instance.StaticRemoteObject;

/**
 * @author Pedro Henrique
 */
public interface StaticCalculator extends StaticRemoteObject {

    default String getIdentifier() {
        return "StaticCalculator";
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
