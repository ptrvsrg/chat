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

public class Controller {

    private final ListeningSupport listeningSupport = new ListeningSupport();
    private Connection connection;
    private DTOHandleService dtoHandleService;

    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    public void removeListener(Listener listener) {
        listeningSupport.removeListener(listener);
    }

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

    public void handleDTO() {
        dtoHandleService.handle();
    }

    public String[] getUsers() {
        if (dtoHandleService == null) {
            return new String[]{};
        }

        DTO response = dtoHandleService.sendRequest(DTO.newUserListRequest());

        if (response.getSubtype() == Subtype.ERROR) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
            return new String[]{};
        }

        return response.getUsers();
    }

    public void sendNewMessage(String message) {
        if (dtoHandleService == null) {
            return;
        }

        DTO response = dtoHandleService.sendRequest(DTO.newNewMessageRequest(message));

        if (response.getSubtype() == Subtype.ERROR) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
        }
    }

    public void logout() {
        if (dtoHandleService == null) {
            return;
        }

        DTO response = dtoHandleService.sendRequest(DTO.newLogoutRequest());

        if (response.getSubtype() == Subtype.ERROR) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
        }

        dtoHandleService.shutdown();
    }
}

