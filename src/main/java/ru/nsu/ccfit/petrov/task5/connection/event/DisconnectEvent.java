package ru.nsu.ccfit.petrov.task5.connection.event;

import java.util.UUID;
import lombok.Getter;

/**
 * The type {@code DisconnectEvent} is class that describes event that occurs when client is disconnected.
 */
@Getter
public class DisconnectEvent
    extends ConnectionEvent {

    private final UUID connectionId;

    /**
     * Instantiates a new {@code DisconnectEvent}.
     *
     * @param connectionId the connection id
     */
    public DisconnectEvent(UUID connectionId) {
        super(ConnectionEventType.DISCONNECT);
        this.connectionId = connectionId;
    }
}
