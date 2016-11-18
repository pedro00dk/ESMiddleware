package test.application.unicastCalculator;

import esm.distribution.instance.RemoteObject;
import esm.distribution.invocation.AbsoluteObjectReference;
import esm.distribution.invocation.Proxy;
import esm.util.Tuple;

import java.io.IOException;

/**
 * @author Pedro Henrique
 */
public class UnicastCalculatorProxy extends Proxy implements UnicastCalculator {

    private boolean instanceCreated;

    private Integer instanceIdentifier;

    private boolean closed;

    public UnicastCalculatorProxy(AbsoluteObjectReference absoluteObjectReference) throws Throwable {
        super(absoluteObjectReference);
    }

    @Override
    public Integer createInstance() {
        throw new UnsupportedOperationException("This operation can not be accessed by a proxy.");
    }

    @Override
    public RemoteObject getInstance(Integer identifier) {
        throw new UnsupportedOperationException("This operation can not be accessed by a proxy.");
    }

    @Override
    public void destroyInstance(Integer identifier) {
        throw new UnsupportedOperationException("This operation can not be accessed by a proxy.");
    }

    @SuppressWarnings("unchecked")
    private void internalCreateInstance() {
        try {
            instanceIdentifier = (Integer) invokeRemotely(
                    "createInstance", new Tuple[]{},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        instanceCreated = true;
    }

    @SuppressWarnings("unchecked")
    private void internalDestroyInstance() {
        try {
            invokeRemotely(
                    "destroyInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
                    false, null, null,
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        instanceCreated = false;
    }

    @Override
    public void close() throws IOException {
        try {
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            internalDestroyInstance();
            closed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer getMem() {
        try {
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            return (Integer) invokeRemotely(
                    "getMem", new Tuple[]{},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            invokeRemotely(
                    "setMem", new Tuple[]{new Tuple<>(a, Integer.class)},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            return (Integer) invokeRemotely(
                    "sum", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            return (Integer) invokeRemotely(
                    "sub", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            return (Integer) invokeRemotely(
                    "mul", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            return (Integer) invokeRemotely(
                    "div", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            return (Integer) invokeRemotely(
                    "mod", new Tuple[]{new Tuple(a, Integer.class), new Tuple(b, Integer.class)},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
            if (closed) {
                throw new IllegalStateException("Remote object already closed.");
            }
            if (!instanceCreated) {
                internalCreateInstance();
            }
            invokeRemotely(
                    "exception", new Tuple[]{},
                    true, "getInstance", new Tuple[]{new Tuple(instanceIdentifier, Integer.class)},
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
