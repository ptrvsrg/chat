package ru.nsu.ccfit.petrov.task5.connection.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;

/**
 * The type {@code ConnectionEvent} is class that describes events that occur while working
 * {@link ru.nsu.ccfit.petrov.task5.connection.Connection Connection} thread.
 */
@Getter
@RequiredArgsConstructor
public abstract class ConnectionEvent
    implements Event {

    /**
     * The type {@code ConnectionEventType} is enum that describes event types that occur while working
     * {@link ru.nsu.ccfit.petrov.task5.connection.Connection Connection} thread.
     */
    public enum ConnectionEventType {
        /**
         * Disconnect event.
         */
        DISCONNECT,
        /**
         * Message received event.
         */
        MESSAGE_RECEIVED
    }

    private final ConnectionEventType type;
}
