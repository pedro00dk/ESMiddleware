package test.application.calculator;

import esm.distribution.instance.DistributedObject;

/**
 * @author Pedro Henrique
 */
public interface Calculator extends DistributedObject {

    default String getIdentifier() {
        return "Calculator";
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
