package ru.nsu.ccfit.petrov.task5.message;

/**
 * The type Message is interface that describes behavior of messages.
 *
 * @author ptrvsrg
 */
public interface Message {

    /**
     * The type {@code MessageType} is enum that describes message type.
     */
    enum MessageType {
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
    enum MessageSubtype {
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

    /**
     * Gets message type.
     *
     * @return the message type
     */
    MessageType getMessageType();

    /**
     * Gets message subtype.
     *
     * @return the message subtype
     */
    MessageSubtype getMessageSubtype();

    /**
     * Get message data.
     *
     * @return the message data
     */
    Object[] getData();
}
