package ru.nsu.ccfit.petrov.task5.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The type {@code Message} is class that describes content of messages.
 *
 * @author ptrvsrg
 */
@Getter
@AllArgsConstructor
public abstract class Message {

    /**
     * The type {@code MessageType} is enum that describes message type.
     */
    public enum MessageType {
        /**
         * Request message type.
         */
        REQUEST,
        /**
         * Response message type.
         */
        RESPONSE,
        /**
         * Event message type.
         */
        EVENT
    }

    /**
     * The type {@code MessageSubtype} is enum that describes message subtype.
     */
    public enum MessageSubtype {
        /**
         * Login message subtype.
         */
        LOGIN,
        /**
         * User list message subtype.
         */
        USER_LIST,
        /**
         * New message message subtype.
         */
        NEW_MESSAGE,
        /**
         * Logout message subtype.
         */
        LOGOUT,
        /**
         * Success message subtype.
         */
        SUCCESS,
        /**
         * Error message subtype.
         */
        ERROR
    }

    protected MessageType messageType;
    protected MessageSubtype messageSubtype;
    protected Object[] data;
}
