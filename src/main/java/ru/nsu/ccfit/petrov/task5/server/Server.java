package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.Message.Subtype;
import ru.nsu.ccfit.petrov.task5.message.Message.Type;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;
import ru.nsu.ccfit.petrov.task5.server.database.ServerDB;

@Log4j2
public class Server {

    private ServerSocket serverSocket;
    private final ServerDB dataBase = new ServerDB();

    public void start() {
        try {
            serverSocket = new ServerSocket(ServerConfig.getPort());
            log.info("Server started");
        } catch (IOException e) {
            log.error("Server didn't start");
            log.catching(Level.ERROR, e);
            return;
        }

        acceptClient();
    }

    public void acceptClient() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            } catch (Exception e) {
                log.error("Connection lost");
                log.catching(Level.ERROR, e);
                break;
            }
        }
    }

    public void sendAll(Message message) {
        for (Connection connection : dataBase.getUsers().values()) {
            try {
                connection.send(message);
            } catch (Exception e) {
                log.error("Message sending error");
                log.catching(Level.ERROR, e);
            }
        }
    }

    private class ServerThread
        extends Thread {

        private final Connection connection;
        private String userName;

        public ServerThread(Socket clientSocket)
            throws IOException {
            clientSocket.setSoTimeout(ServerConfig.getTimeout());
            this.connection = new Connection(clientSocket);
        }

        @Override
        public void run() {
            if (!addClient()) {
                return;
            }

            handleRequests();
        }

        private boolean addClient() {
            while (true) {
                try {
                    Message request = connection.receive();
                    if (request == null || request.getType() != Type.REQUEST ||
                        request.getSubtype() != Subtype.LOGIN) {
                        continue;
                    }

                    userName = request.getUserName();
                    if (dataBase.getUsers().containsKey(userName)) {
                        connection.send(
                            Message.newErrorResponse(request.getId(), "Username already used"));
                        continue;
                    }

                    dataBase.addUser(userName, connection);
                    connection.send(Message.newSuccessResponse(request.getId()));

                    return true;
                } catch (SocketTimeoutException e) {
                    log.error("Receive timed out");
                    log.catching(Level.ERROR, e);

                    closeConnection();

                    return false;
                } catch (IOException e) {
                    log.error("Client adding error");
                    log.catching(Level.ERROR, e);
                }
            }
        }

        private void handleRequests() {
            while (true) {
                try {
                    Message request = connection.receive();
                    if (request.getType() != Type.REQUEST) {
                        return;
                    }

                    if (request.getSubtype() == Subtype.NEW_MESSAGE) {
                        connection.send(Message.newSuccessResponse(request.getId()));

                        String messageContent = request.getMessageContent();
                        sendAll(Message.newNewMessageEvent(userName, messageContent));
                    }

                    if (request.getSubtype() == Subtype.USER_LIST) {
                        connection.send(Message.newSuccessResponse(request.getId(),
                                                                   dataBase.getUsers().keySet()));
                    }

                    if (request.getSubtype() == Subtype.LOGOUT) {
                        connection.send(Message.newSuccessResponse(request.getId()));

                        sendAll(Message.newLoginEvent(userName));

                        dataBase.removeUser(userName);
                        closeConnection();
                        return;
                    }
                } catch (IOException e) {
                    log.error("Message sending error");
                    log.catching(Level.ERROR, e);
                    closeConnection();
                    return;
                }
            }
        }

        private void closeConnection() {
            try {
                connection.close();
            } catch (IOException e) {
                log.error("Connection closing error");
                log.catching(Level.ERROR, e);
            }
        }
    }
}