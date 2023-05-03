package ru.nsu.ccfit.petrov.task5.server.event;

import java.util.UUID;
import lombok.Getter;

/**
 * The type {@code NewMessageRequestReceivedEvent} is class that describes event when new message request is received.
 *
 * @author ptrvsrg
 */
@Getter
public class NewMessageRequestReceivedEvent
    extends ServerEvent {

    private final UUID connectionId;
    private final String chatName;
    private final UUID sessionId;
    private final String message;

    /**
     * Instantiates a new {@code NewMessageRequestReceivedEvent}.
     *
     * @param connectionId the connection id
     * @param chatName     the chat name
     * @param sessionId    the session id
     * @param message      the message
     */
    public NewMessageRequestReceivedEvent(UUID connectionId, String chatName, UUID sessionId, String message) {
        super(ServerEventType.NEW_MESSAGE_REQUEST_RECEIVED);
        this.connectionId = connectionId;
        this.chatName = chatName;
        this.sessionId = sessionId;
        this.message = message;
    }
}
