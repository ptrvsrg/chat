package ru.nsu.ccfit.petrov.task5.message.object;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code JavaObjectMessage} is class that implements {@link Message} and {@link Serializable} for sending Java
 * object message over sockets.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class JavaObjectMessage
    implements Message, Serializable {

    private final MessageType messageType;
    private final MessageSubtype messageSubtype;
    private final Object[] data;
}
