package ru.nsu.ccfit.petrov.chat.client.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import ru.nsu.ccfit.petrov.chat.client.controller.service.DTOHandleService;
import ru.nsu.ccfit.petrov.chat.client.controller.service.RegisterService;
import ru.nsu.ccfit.petrov.chat.client.listener.Listener;
import ru.nsu.ccfit.petrov.chat.client.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.chat.client.listener.event.ErrorEvent;
import ru.nsu.ccfit.petrov.chat.client.controller.config.ClientConfig;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.connection.ConnectionFactory;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Subtype;

/**
 * The type Controller is class that provides interaction between the server and the GUI.
 *
 * @author ptrvsrg
 */
public class Controller {

    private final ListeningSupport listeningSupport = new ListeningSupport();
    private Connection connection;
    private DTOHandleService dtoHandleService;

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(Listener listener) {
        listeningSupport.removeListener(listener);
    }

    /**
     * Connect to server.
     *
     * @param serverAddress the server address
     * @param serverPort    the server port
     * @return {@code true} if client successfully connected, {@code false} otherwise
     */
    public boolean connect(InetAddress serverAddress, int serverPort) {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException ignores) {}
        }

        try {
            Socket clientSocket = new Socket(serverAddress, serverPort);
            connection = ConnectionFactory.newConnection(ClientConfig.getDTOFormat());
            connection.connect(clientSocket);
        } catch (IOException e) {
            listeningSupport.notifyListeners(new ErrorEvent("Server is not available"));
            return false;
        }

        return true;
    }

    /**
     * Login.
     *
     * @param username the username
     * @return {@code true} if client successfully registered, {@code false} otherwise
     */
    public boolean login(String username) {
        if (connection == null) {
            return false;
        }

        RegisterService registerService = new RegisterService(listeningSupport);
        boolean registerResult = registerService.register(connection, username);
        registerService.shutdown();

        if (registerResult) {
            dtoHandleService = new DTOHandleService(username, connection, listeningSupport);
        }

        return registerResult;
    }

    /**
     * Start handling DTO.
     */
    public void handleDTO() {
        dtoHandleService.handle();
    }

    /**
     * Get users.
     *
     * @return the users array
     */
    public String[] getUsers() {
        if (dtoHandleService == null) {
            listeningSupport.notifyListeners(new ErrorEvent("Сlient not registered"));
            return new String[]{};
        }

        DTO response = dtoHandleService.sendRequest(DTO.newUserListRequest());

        if (response.getSubtype() == Subtype.ERROR) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
            return new String[]{};
        }

        return response.getUsers();
    }

    /**
     * Send new user message.
     *
     * @param message the message
     */
    public void sendNewMessage(String message) {
        if (dtoHandleService == null) {
            listeningSupport.notifyListeners(new ErrorEvent("Сlient not registered"));
            return;
        }

        DTO response = dtoHandleService.sendRequest(DTO.newNewMessageRequest(message));

        if (response.getSubtype() == Subtype.ERROR) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
        }
    }

    /**
     * Logout.
     */
    public void logout() {
        if (dtoHandleService == null) {
            listeningSupport.notifyListeners(new ErrorEvent("Сlient not registered"));
            return;
        }

        DTO response = dtoHandleService.sendRequest(DTO.newLogoutRequest());

        if (response.getSubtype() == Subtype.ERROR) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
        }

        dtoHandleService.shutdown();
    }
}

