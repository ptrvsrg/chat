package ru.nsu.ccfit.petrov.task5.client.model;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import ru.nsu.ccfit.petrov.task5.client.model.config.ClientConfig;
import ru.nsu.ccfit.petrov.task5.client.model.handler.MessageHandler;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.client.listener.Listener;
import ru.nsu.ccfit.petrov.task5.client.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.client.listener.event.ClientErrorEvent;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.Message.Subtype;

public class Client {

    private final ListeningSupport listeningSupport = new ListeningSupport();
    private MessageHandler messageHandler;

    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    public void start() {
        try {
            Socket clientSocket = new Socket(ClientConfig.getHostName(), ClientConfig.getPort());
            clientSocket.setSoTimeout(ClientConfig.getTimeout());
            messageHandler = new MessageHandler(new Connection(clientSocket), listeningSupport);
        } catch (IOException e) {
            closeClient("Server is not available", true);
        }
    }

    public boolean login(String userName) {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newLoginRequest(userName));
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error", true);
            return false;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason, false);
            return false;
        }

        return true;
    }

    public Set<String> getUsers() {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newUserListRequest());
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error", true);
            return new HashSet<>();
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason, false);
            return new HashSet<>();
        }

        return Set.of(response.getUsers());
    }

    public void sendUserMessage(String messageText) {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newNewMessageRequest(messageText));
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error", true);
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason, false);
        }
    }

    public void logout() {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newLogoutRequest());
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error", true);
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason, false);
        } else {
            messageHandler.shutdown();
        }
    }

    private void closeClient(String reason, boolean terminated) {
        listeningSupport.notifyListeners(new ClientErrorEvent(reason, terminated));
        if (terminated && messageHandler != null) {
            messageHandler.shutdown();
        }
    }
}

