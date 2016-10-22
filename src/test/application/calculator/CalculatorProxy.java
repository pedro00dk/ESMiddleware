package test.application.calculator;

import esm.distribution.invocation.Proxy;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.util.Tuple;

/**
 * @author Pedro Henrique
 */
public class CalculatorProxy extends Proxy implements Calculator {

    public CalculatorProxy(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer getMem() {
        try {
            return (Integer) invokeRemotely("getMem", true,
                    new Tuple[]{}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void setMem(Integer a) {
        try {
            invokeRemotely("setMem", true,
                    new Tuple[]{new Tuple<>(a, Integer.class)}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer sum(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely("sum", true,
                    new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer sub(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely("sub", true,
                    new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer mul(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely("mul", true,
                    new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer div(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely("div", true,
                    new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer mod(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely("mod", true,
                    new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)}
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exception() throws UnsupportedOperationException {
        try {
            invokeRemotely("exception", true,
                    new Tuple[]{}
            );
        } catch (Throwable throwable) {
            if (throwable instanceof UnsupportedOperationException) {
                throw (UnsupportedOperationException) throwable;
            }
            throwable.printStackTrace();
        }
    }
}
