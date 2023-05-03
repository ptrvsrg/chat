package ru.nsu.ccfit.petrov.task5.server.event;

import java.util.UUID;
import lombok.Getter;

/**
 * The type {@code LoginRequestReceivedEvent} is class that describes event when login request is received.
 *
 * @author ptrvsrg
 */
@Getter
public class LoginRequestReceivedEvent
    extends ServerEvent {

    private final UUID connectionId;
    private final String chatName;
    private final String userName;

    /**
     * Instantiates a new {@code LoginRequestReceivedEvent}.
     *
     * @param connectionId the connection id
     * @param chatName     the chat name
     * @param userName     the username
     */
    public LoginRequestReceivedEvent(UUID connectionId, String chatName, String userName) {
        super(ServerEventType.LOGIN_REQUEST_RECEIVED);
        this.connectionId = connectionId;
        this.chatName = chatName;
        this.userName = userName;
    }
}
