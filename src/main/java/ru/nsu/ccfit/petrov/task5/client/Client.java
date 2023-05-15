package ru.nsu.ccfit.petrov.task5.client;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import ru.nsu.ccfit.petrov.task5.client.config.ClientConfig;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.listener.event.ClientErrorEvent;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.Message.Subtype;

public class Client {

    private final ListeningSupport listeningSupport = new ListeningSupport();
    private MessageHandler messageHandler;

    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    public void login(String userName) {
        try {
            Socket clientSocket = new Socket(ClientConfig.getHostName(), ClientConfig.getPort());
            clientSocket.setSoTimeout(ClientConfig.getTimeout());
            messageHandler = new MessageHandler(new Connection(clientSocket), listeningSupport);
        } catch (IOException e) {
            closeClient("Server is not available");
            return;
        }

        Message response;
        try {
            response = messageHandler.sendRequest(Message.newLoginRequest(userName));
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error");
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason);
        }
    }

    public Set<String> getUsers() {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newUserListRequest());
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error");
            return new HashSet<>();
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason);
            return new HashSet<>();
        }

        return Set.of(response.getUsers());
    }

    public void sendUserMessage(String messageText) {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newNewMessageRequest(messageText));
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error");
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason);
        }
    }

    public void logout() {
        Message response;
        try {
            response = messageHandler.sendRequest(Message.newLogoutRequest());
        } catch (ExecutionException | InterruptedException e) {
            closeClient("Connection error");
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            closeClient(reason);
        } else {
            messageHandler.shutdown();
        }
    }

    private void closeClient(String reason) {
        listeningSupport.notifyListeners(new ClientErrorEvent(reason));
        if (messageHandler != null) {
            messageHandler.shutdown();
        }
    }
}

