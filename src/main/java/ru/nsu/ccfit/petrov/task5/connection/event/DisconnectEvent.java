package ru.nsu.ccfit.petrov.task5.connection.event;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The type {@code DisconnectEvent} is class that describes event that occurs when client is disconnected.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class DisconnectEvent
    implements ConnectionEvent {

    private final UUID connectionId;
}
