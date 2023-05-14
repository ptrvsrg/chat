package ru.nsu.ccfit.petrov.task5.client;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.nsu.ccfit.petrov.task5.client.config.ClientConfig;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.listener.event.ClientErrorEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.LoginEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.LogoutEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.NewMessageEvent;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.Message.Subtype;
import ru.nsu.ccfit.petrov.task5.message.Message.Type;

public class Client {

    private static final int THREAD_COUNT = 4;
    private final ListeningSupport listeningSupport = new ListeningSupport();
    private final ExecutorService messageHandler = Executors.newSingleThreadExecutor();
    private final ExecutorService requestSender = Executors.newFixedThreadPool(THREAD_COUNT);
    private final Map<UUID, CompletableFuture<Message>> requests = new ConcurrentHashMap<>();
    private Connection connection;

    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    private Message sendRequest(Message request) {
        CompletableFuture<Message> futureResponse = new CompletableFuture<>();
        requests.put(request.getId(), futureResponse);
        CompletableFuture.runAsync(() -> {
            try {
                connection.send(request);
            } catch (IOException e) {
                futureResponse.completeExceptionally(e);
            }
        }, requestSender);

        try {
            return futureResponse.get();
        } catch (ExecutionException | InterruptedException e) {
            listeningSupport.notifyListeners(new ClientErrorEvent("Messaging error"));
            return null;
        }
    }

    private void handleMessage() {
        CompletableFuture.runAsync(() -> {
            while (!messageHandler.isShutdown()) {
                try {
                    Message message = connection.receive();
                    if (message == null) {
                        continue;
                    }

                    if (message.getType() == Type.EVENT) {
                        processEvent(message);
                    } else if (message.getType() == Type.RESPONSE) {
                        CompletableFuture<Message> futureResponse = requests.remove(
                            message.getRequestId());
                        futureResponse.complete(message);
                    }
                } catch (IOException e) {
                    listeningSupport.notifyListeners(new ClientErrorEvent("Messaging error"));
                }
            }
        }, messageHandler);
    }

    private void processEvent(Message event) {
        if (event.getSubtype() == Subtype.LOGIN) {
            listeningSupport.notifyListeners(
                new LoginEvent(event.getUserName()));
        } else if (event.getSubtype() == Subtype.NEW_MESSAGE) {
            listeningSupport.notifyListeners(
                new NewMessageEvent(event.getUserName(), event.getMessageContent()));
        } else if (event.getSubtype() == Subtype.LOGOUT) {
            listeningSupport.notifyListeners(
                new LogoutEvent(event.getUserName()));
        }
    }

    public void login(String userName) {
        try {
            Socket clientSocket = new Socket(ClientConfig.getHostName(), ClientConfig.getPort());
            clientSocket.setSoTimeout(ClientConfig.getTimeout());
            connection = new Connection(clientSocket);
        } catch (IOException e) {
            listeningSupport.notifyListeners(new ClientErrorEvent("Server is not available"));
            return;
        }

        handleMessage();

        Message response = sendRequest(Message.newLoginRequest(userName));
        if (response == null) {
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            listeningSupport.notifyListeners(new ClientErrorEvent(reason));
        }
    }

    public Set<String> getUsers() {
        Message response = sendRequest(Message.newUserListRequest());
        if (response == null) {
            return new HashSet<>();
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            listeningSupport.notifyListeners(new ClientErrorEvent(reason));
            return new HashSet<>();
        }

        return response.getUsers();
    }

    public void sendUserMessage(String messageText) {
        Message response = sendRequest(Message.newNewMessageRequest(messageText));
        if (response == null) {
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            listeningSupport.notifyListeners(new ClientErrorEvent(reason));
        }
    }

    public void logout() {
        Message response = sendRequest(Message.newLogoutRequest());
        if (response == null) {
            return;
        }

        if (response.getSubtype() == Subtype.ERROR) {
            String reason = response.getMessageContent();
            listeningSupport.notifyListeners(new ClientErrorEvent(reason));
        }

        requestSender.shutdownNow();
        messageHandler.shutdownNow();
        try {
            connection.close();
        } catch (IOException e) {
            listeningSupport.notifyListeners(new ClientErrorEvent("Connection closing error"));
        }
    }
}
