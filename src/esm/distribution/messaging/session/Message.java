package esm.distribution.messaging.session;

import java.io.Serializable;

/**
 * @author Pedro Henrique
 */
public class Message implements Serializable {

    private final String magic = "phSessionProtocol";
    private String version = "1.0";
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    private MessageType messageType = MessageType.JAVA_OBJECT;
    private long messageSize = 0;

    private Object body;

    public Message(Object body) {
        this.body = body;
    }

    public Message(String version, ByteOrder byteOrder, MessageType messageType, long messageSize,
                   Object body) {
        this.version = version;
        this.byteOrder = byteOrder;
        this.messageType = messageType;
        this.messageSize = messageSize;
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    enum ByteOrder {
        LITTLE_ENDIAN, BIG_ENDIAN
    }

    enum MessageType {
        BYTE_ARRAY, JAVA_OBJECT
    }
}
