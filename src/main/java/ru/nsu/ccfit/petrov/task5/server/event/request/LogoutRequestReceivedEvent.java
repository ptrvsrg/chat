package ru.nsu.ccfit.petrov.task5.server.event.request;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.server.event.ServerEvent;

/**
 * The type {@code LogoutRequestReceivedEvent} is class that describes event when logout request is received.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class LogoutRequestReceivedEvent
    implements ServerEvent {

    private final UUID connectionId;
    private final String chatName;
    private final UUID sessionId;
}
