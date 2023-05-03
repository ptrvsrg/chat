package ru.nsu.ccfit.petrov.task5.message;

import java.util.List;
import java.util.UUID;

/**
 * The type {@code MessageGenerator} is interface that generates messages in specified format.
 *
 * @author ptrvsrg 
 */
public interface MessageGenerator {

    /**
     * Creates login event message.
     *
     * @param chatName the chat name
     * @param userName the username
     * @return the message, or {@code null} - invalid arguments
     */
    Message createLoginEventMessage(String chatName, String userName);

    /**
     * Creates new message event message.
     *
     * @param chatName the chat name
     * @param userName the username
     * @param message  the message
     * @return the message, or {@code null} - invalid arguments
     */
    Message createNewMessageEventMessage(String chatName, String userName, String message);

    /**
     * Creates logout event message.
     *
     * @param chatName the chat name
     * @param userName the username
     * @return the message, or {@code null} - invalid arguments
     */
    Message createLogoutEventMessage(String chatName, String userName);

    /**
     * Creates success response message.
     *
     * @return the message, or {@code null} - invalid arguments
     */
    Message createSuccessResponseMessage();

    /**
     * Creates success response message.
     *
     * @param sessionId the session id
     * @return the message, or {@code null} - invalid arguments
     */
    Message createSuccessResponseMessage(UUID sessionId);

    /**
     * Creates success response message.
     *
     * @param chatName  the chat name
     * @param userNames the username list
     * @return the message, or {@code null} - invalid arguments
     */
    Message createSuccessResponseMessage(String chatName, List<String> userNames);

    /**
     * Creates error response message.
     *
     * @param reason the reason
     * @return the message, or {@code null} - invalid arguments
     */
    Message createErrorResponseMessage(String reason);

    /**
     * Creates login request message.
     *
     * @param chatName the chat name
     * @param userName the username
     * @return the message, or {@code null} - invalid arguments
     */
    Message createLoginRequestMessage(String chatName, String userName);

    /**
     * Creates new message request message.
     *
     * @param chatName  the chat name
     * @param sessionId the session id
     * @param message   the message
     * @return the message, or {@code null} - invalid arguments
     */
    Message createNewMessageRequestMessage(String chatName, UUID sessionId, String message);

    /**
     * Creates user list request message.
     *
     * @param chatName  the chat name
     * @param sessionId the session id
     * @return the message, or {@code null} - invalid arguments
     */
    Message createUserListRequestMessage(String chatName, UUID sessionId);

    /**
     * Creates logout request message.
     *
     * @param chatName  the chat name
     * @param sessionId the session id
     * @return the message, or {@code null} - invalid arguments
     */
    Message createLogoutRequestMessage(String chatName, UUID sessionId);
}
