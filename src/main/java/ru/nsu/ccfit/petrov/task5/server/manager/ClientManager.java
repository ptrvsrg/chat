package ru.nsu.ccfit.petrov.task5.server.manager;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.Message.Subtype;
import ru.nsu.ccfit.petrov.task5.message.Message.Type;
import ru.nsu.ccfit.petrov.task5.server.database.ServerDB;

@Log4j2
public class ClientManager {

    private static final int CLIENT_HANDLER_COUNT = 4;
    private final ExecutorService clientHandlers =
        Executors.newFixedThreadPool(CLIENT_HANDLER_COUNT);
    private final ServerDB dataBase = new ServerDB();

    public void addClient(Connection connection) {
        clientHandlers.execute(new RegisterTask(connection));
    }

    private void sendEvent(Message message) {
        for (Connection connection : dataBase.getUsers().values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                log.info("Message didn't send");
                log.catching(Level.INFO, e);
                closeConnection(connection);
            }
        }
    }

    private synchronized void closeConnection(Connection connection) {
        connection.close();

        String userName = dataBase.getUserByConnection(connection);
        if (userName != null) {
            dataBase.removeUser(userName);
            sendEvent(Message.newLogoutEvent(userName));
        }
    }

    @RequiredArgsConstructor
    private class RegisterTask
        implements Runnable {

        private final Connection connection;

        @Override
        public void run() {
            while (true) {
                try {
                    Message request = connection.receive();
                    if (request == null ||
                        request.getType() != Type.REQUEST ||
                        request.getSubtype() != Subtype.LOGIN) {
                        continue;
                    }

                    String userName = request.getUserName();
                    UUID requestId = request.getId();

                    if (userName == null || userName.isEmpty()) {
                        connection.send(
                            Message.newErrorResponse(requestId, "Username format is invalid"));
                        continue;
                    } else if (dataBase.getUsers().containsKey(userName)) {
                        connection.send(
                            Message.newErrorResponse(requestId, "Username already used"));
                        continue;
                    }

                    dataBase.addUser(userName, connection);
                    connection.send(Message.newSuccessResponse(requestId));

                    sendEvent(Message.newLoginEvent(userName));

                    break;
                } catch (IOException e) {
                    log.info("Client didn't add");
                    log.catching(Level.INFO, e);
                    closeConnection(connection);
                    return;
                }
            }

            clientHandlers.execute(new RequestHandleTask(connection));
        }
    }

    @RequiredArgsConstructor
    private class RequestHandleTask
        implements Runnable {

        private final Connection connection;

        @Override
        public void run() {
            String userName = dataBase.getUserByConnection(connection);
            if (userName == null) {
                return;
            }

            try {
                Message request = connection.receive();
                if (request.getType() != Type.REQUEST) {
                    return;
                }

                switch (request.getSubtype()) {
                    case NEW_MESSAGE: {
                        UUID requestId = request.getId();
                        String messageContent = request.getMessageContent();

                        connection.send(Message.newSuccessResponse(requestId));
                        sendEvent(Message.newNewMessageEvent(userName, messageContent));
                        break;
                    }
                    case USER_LIST: {
                        UUID requestId = request.getId();
                        Set<String> users = dataBase.getUsers().keySet();

                        connection.send(Message.newSuccessResponse(requestId, users));
                        break;
                    }
                    case LOGOUT: {
                        UUID requestId = request.getId();

                        connection.send(Message.newSuccessResponse(requestId));
                        sendEvent(Message.newLogoutEvent(userName));

                        dataBase.removeUser(userName);
                        closeConnection(connection);
                        return;
                    }
                }

                clientHandlers.execute(this);
            } catch (IOException e) {
                log.info("Message handling aborted");
                log.catching(Level.INFO, e);
                closeConnection(connection);
            }
        }
    }
}
