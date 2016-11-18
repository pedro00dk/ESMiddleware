package test.application.staticCalculator;

import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.util.Tuple;

/**
 * @author Pedro Henrique
 */
public class StaticCalculatorProxy extends Proxy implements StaticCalculator {

    public StaticCalculatorProxy(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer getMem() {
        try {
            return (Integer) invokeRemotely(
                    "getMem", new Tuple[]{},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void setMem(Integer a) {
        try {
            invokeRemotely(
                    "setMem", new Tuple[]{new Tuple<>(a, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer sum(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely(
                    "sum", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer sub(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely(
                    "sub", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer mul(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely(
                    "mul", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer div(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely(
                    "div", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer mod(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely(
                    "mod", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exception() throws UnsupportedOperationException {
        try {
            invokeRemotely(
                    "exception", new Tuple[]{},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            if (e instanceof UnsupportedOperationException) {
                throw (UnsupportedOperationException) e;
            }
            e.printStackTrace();
        }
    }
}
