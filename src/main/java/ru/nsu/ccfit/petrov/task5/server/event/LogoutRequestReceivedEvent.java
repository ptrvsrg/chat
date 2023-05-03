package ru.nsu.ccfit.petrov.task5.server.event;

import java.util.UUID;
import lombok.Getter;

/**
 * The type {@code LogoutRequestReceivedEvent} is class that describes event when logout request is received.
 *
 * @author ptrvsrg
 */
@Getter
public class LogoutRequestReceivedEvent
    extends ServerEvent {

    private final UUID connectionId;
    private final String chatName;
    private final UUID sessionId;

    /**
     * Instantiates a new {@code LogoutRequestReceivedEvent}.
     *
     * @param connectionId the connection id
     * @param chatName     the chat name
     * @param sessionId    the session id
     */
    public LogoutRequestReceivedEvent(UUID connectionId, String chatName, UUID sessionId) {
        super(ServerEventType.LOGOUT_REQUEST_RECEIVED);
        this.connectionId = connectionId;
        this.chatName = chatName;
        this.sessionId = sessionId;
    }
}
