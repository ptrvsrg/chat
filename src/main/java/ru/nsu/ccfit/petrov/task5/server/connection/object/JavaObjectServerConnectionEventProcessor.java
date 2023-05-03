package ru.nsu.ccfit.petrov.task5.server.connection.object;

import java.util.Map;
import java.util.UUID;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.server.connection.ServerConnectionEventProcessor;
import ru.nsu.ccfit.petrov.task5.server.event.LoginRequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.server.event.LogoutRequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.server.event.NewMessageRequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.server.event.UserListRequestReceivedEvent;

/**
 * The type {@code JavaObjectServerConnectionEventProcessor} is class that implements abstract method of
 * {@link ServerConnectionEventProcessor} for processing Java object messages.
 *
 * @author ptrvsrg
 */
public class JavaObjectServerConnectionEventProcessor
    extends ServerConnectionEventProcessor {

    /**
     * Instantiates a new {@code JavaObjectServerConnectionEventProcessor}.
     *
     * @param connections the connections
     */
    public JavaObjectServerConnectionEventProcessor(Map<UUID, Connection> connections) {
        super(connections);
    }

    @Override
    protected void processLoginRequest(UUID connectionId, Object[] data) {
        try {
            listeningSupport.notifyListeners(
                new LoginRequestReceivedEvent(connectionId, (String) data[0], (String) data[1]));
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    protected void processUserListRequest(UUID connectionId, Object[] data) {
        try {
            listeningSupport.notifyListeners(
                new UserListRequestReceivedEvent(connectionId, (String) data[0], (UUID) data[1]));
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    protected void processNewMessageRequest(UUID connectionId, Object[] data) {
        try {
            listeningSupport.notifyListeners(
                new NewMessageRequestReceivedEvent(connectionId, (String) data[0], (UUID) data[1], (String) data[2]));
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    protected void processLogoutRequest(UUID connectionId, Object[] data) {
        try {
            listeningSupport.notifyListeners(
                new LogoutRequestReceivedEvent(connectionId, (String) data[0], (UUID) data[1]));
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ignored) {
        }
    }
}
