package ru.nsu.ccfit.petrov.task5.server.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.listener.Event;

/**
 * The type {@code ServerEvent} is class that describes server events.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public abstract class ServerEvent
    implements Event {

    /**
     * The type {@code ServerEventType} is enum that describes server event types.
     */
    public enum ServerEventType {
        /**
         * Login request received server event type.
         */
        LOGIN_REQUEST_RECEIVED,
        /**
         * New message request received server event type.
         */
        NEW_MESSAGE_REQUEST_RECEIVED,
        /**
         * User list request received server event type.
         */
        USER_LIST_REQUEST_RECEIVED,
        /**
         * Logout request received server event type.
         */
        LOGOUT_REQUEST_RECEIVED,
        /**
         * Session timeout server event type.
         */
        SESSION_TIMEOUT,
        /**
         * Timer finished server event type.
         */
        STOPWATCH_FINISHED
    }

    private final ServerEventType type;
}
