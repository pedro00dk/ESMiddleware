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
     * @throws IOException if an I/O exception of some sort has occurred
     */
    public static byte[] marshall(Object objData) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(objData);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Unmarshalls the received data.
     *
     * @param data the byte array to be processed
     * @return the deserialized object
     * @throws IOException            if an I/O exception of some sort has occurred
     * @throws ClassNotFoundException if the class type was not found
     */
    @SuppressWarnings("unchecked")
    public static Object unmarshall(byte[] data) throws IOException, ClassNotFoundException {
        return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
    }
}
