package ru.nsu.ccfit.petrov.task5.server.event;

import java.util.UUID;
import lombok.Getter;

/**
 * The type {@code UserListRequestReceivedEvent} is class that describes event when user list request is received.
 *
 * @author ptrvsrg
 */
@Getter
public class UserListRequestReceivedEvent
    extends ServerEvent {

    private final UUID connectionId;
    private final String chatName;
    private final UUID sessionId;

    /**
     * Instantiates a new {@code UserListRequestReceivedEvent}.
     *
     * @param connectionId the connection id
     * @param chatName     the chat name
     * @param sessionId    the session id
     */
    public UserListRequestReceivedEvent(UUID connectionId, String chatName, UUID sessionId) {
        super(ServerEventType.USER_LIST_REQUEST_RECEIVED);
        this.connectionId = connectionId;
        this.chatName = chatName;
        this.sessionId = sessionId;
    }
}
