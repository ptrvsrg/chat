package ru.nsu.ccfit.petrov.task5.server.event.request;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.server.event.ServerEvent;

/**
 * The type {@code NewMessageRequestReceivedEvent} is class that describes event when new message request is received.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class NewMessageRequestReceivedEvent
    implements ServerEvent {

    private final UUID connectionId;
    private final String chatName;
    private final UUID sessionId;
    private final String message;
}
