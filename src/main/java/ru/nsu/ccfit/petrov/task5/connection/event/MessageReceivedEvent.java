package ru.nsu.ccfit.petrov.task5.connection.event;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code MessageReceivedEvent} is class that describes event that occurs when server receives message from
 * client.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class MessageReceivedEvent
    implements ConnectionEvent {

    private final UUID connectionId;
    private final Message message;
}
