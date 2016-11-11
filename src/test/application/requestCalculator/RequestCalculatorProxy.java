package test.application.requestCalculator;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.util.Tuple;

/**
 * @author Pedro Henrique
 */
public class RequestCalculatorProxy extends Proxy implements RequestCalculator {

    public RequestCalculatorProxy(AbsoluteObjectReference absoluteObjectReference) {
        super(absoluteObjectReference);
    }

    @Override
    public RemoteObject createInstance() {
        throw new UnsupportedOperationException("This operation can not be accessed by a proxy.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer getMem() {
        try {
            return (Integer) invokeRemotely(
                    "getMem", new Tuple[]{},
                    true, "createInstance", new Tuple[]{},
                    true
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
            invokeRemotely(
                    "setMem", new Tuple[]{new Tuple<>(a, Integer.class)},
                    true, "createInstance", new Tuple[]{},
                    true
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer sum(Integer a, Integer b) {
        try {
            return (Integer) invokeRemotely(
                    "sum", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "createInstance", new Tuple[]{},
                    true
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
            return (Integer) invokeRemotely(
                    "sub", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "createInstance", new Tuple[]{},
                    true
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
            return (Integer) invokeRemotely(
                    "mul", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "createInstance", new Tuple[]{},
                    true
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
            return (Integer) invokeRemotely(
                    "div", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "createInstance", new Tuple[]{},
                    true
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
            return (Integer) invokeRemotely(
                    "mod", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "createInstance", new Tuple[]{},
                    true
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
            invokeRemotely(
                    "exception", new Tuple[]{},
                    true, "createInstance", new Tuple[]{},
                    true
            );
        } catch (Throwable throwable) {
            if (throwable instanceof UnsupportedOperationException) {
                throw (UnsupportedOperationException) throwable;
            }
            throwable.printStackTrace();
        }
    }
}
