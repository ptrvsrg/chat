package ru.nsu.ccfit.petrov.task5.message.object;

import java.io.Serializable;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code JavaObjectMessage} is class that extends {@link Message} and implements {@link Serializable} for
 * sending Java object message over sockets.
 *
 * @author ptrvsrg
 */
public class JavaObjectMessage
    extends Message
    implements Serializable {

    public JavaObjectMessage(MessageType messageType, MessageSubtype messageSubtype, Object[] data) {
        super(messageType, messageSubtype, data);
    }
}
