package esm.distribution.serialization;

import java.io.*;

/**
 * Class that process objects and byte arrays to be used in the messaging over the network.
 *
 * @author Pedro Henrique
 */
public final class Marshaller {

    /**
     * Prevents instantiation.
     */
    private Marshaller() {
    }

    /**
     * Marshalls the received object.
     *
     * @param objData the object to serialize
     * @return the serialized object
     */
    public static byte[] marshall(Object objData) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(objData);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error();
        }
    }

    /**
     * Unmarshalls the received data.
     *
     * @param data the byte array to be processed
     * @return the deserialized object
     */
    @SuppressWarnings("unchecked")
    public static Object unmarshall(byte[] data) {
        try {
            return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
