package ru.nsu.ccfit.petrov.task5.connection.event;

import java.util.UUID;
import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code MessageReceivedEvent} is class that describes event that occurs when server receives message from
 * client.
 *
 * @author ptrvsrg
 */
@Getter
public class MessageReceivedEvent
    extends ConnectionEvent {

    private final UUID connectionId;
    private final Message message;

    /**
     * Instantiates a new {@code MessageReceivedEvent}.
     *
     * @param connectionId the connection id
     * @param message      the message
     */
    public MessageReceivedEvent(UUID connectionId, Message message) {
        super(ConnectionEventType.MESSAGE_RECEIVED);
        this.connectionId = connectionId;
        this.message = message;
    }
}
