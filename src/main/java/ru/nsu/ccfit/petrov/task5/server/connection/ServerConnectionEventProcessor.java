package ru.nsu.ccfit.petrov.task5.server.connection;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.connection.event.DisconnectEvent;
import ru.nsu.ccfit.petrov.task5.connection.event.MessageReceivedEvent;
import ru.nsu.ccfit.petrov.task5.connection.event.processor.ConnectionEventProcessor;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code ServerConnectionEventProcessor} is class that processes connection event.
 *
 * @author ptrvsrg
 */
@RequiredArgsConstructor
public abstract class ServerConnectionEventProcessor
    implements ConnectionEventProcessor {

    private final Map<UUID, Connection> connections;
    protected final ListeningSupport listeningSupport = new ListeningSupport();

    @Override
    public void processEvent(DisconnectEvent event) {
        UUID connectionId = event.getConnectionId();
        connections.remove(connectionId);
    }

    @Override
    public void processEvent(MessageReceivedEvent event) {
        UUID connectionId = event.getConnectionId();
        Message message = event.getMessage();
        Object[] data = message.getData();
        switch (message.getMessageSubtype()) {
            case LOGIN:
                processLoginRequest(connectionId, data);
                break;
            case USER_LIST:
                processUserListRequest(connectionId, data);
                break;
            case NEW_MESSAGE:
                processNewMessageRequest(connectionId, data);
                break;
            case LOGOUT:
                processLogoutRequest(connectionId, data);
                break;
        }
    }

    /**
     * Processes login request.
     *
     * @param connectionId the connection id
     * @param data         the message data
     */
    protected abstract void processLoginRequest(UUID connectionId, Object[] data);

    /**
     * Processes user list request.
     *
     * @param connectionId the connection id
     * @param data         the message data
     */
    protected abstract void processUserListRequest(UUID connectionId, Object[] data);

    /**
     * Processes new message request.
     *
     * @param connectionId the connection id
     * @param data         the message data
     */
    protected abstract void processNewMessageRequest(UUID connectionId, Object[] data);

    /**
     * Processes logout request.
     *
     * @param connectionId the connection id
     * @param data         the message data
     */
    protected abstract void processLogoutRequest(UUID connectionId, Object[] data);

    /**
     * Adds listener.
     *
     * @param listener the listener
     */
    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }
}
