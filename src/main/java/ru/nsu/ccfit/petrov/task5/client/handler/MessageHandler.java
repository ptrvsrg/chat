package ru.nsu.ccfit.petrov.task5.client.handler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.client.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.client.listener.event.ClientErrorEvent;
import ru.nsu.ccfit.petrov.task5.client.listener.event.LoginEvent;
import ru.nsu.ccfit.petrov.task5.client.listener.event.LogoutEvent;
import ru.nsu.ccfit.petrov.task5.client.listener.event.NewMessageEvent;
import ru.nsu.ccfit.petrov.task5.message.Message;

public class MessageHandler {

    private static final int SENDER_COUNT = 1;
    private static final int RECEIVER_COUNT = 1;
    private final Map<UUID, CompletableFuture<Message>> requests = new ConcurrentHashMap<>();
    private final ExecutorService sender = Executors.newFixedThreadPool(SENDER_COUNT);
    private final ExecutorService receiver = Executors.newFixedThreadPool(RECEIVER_COUNT);
    private final Connection connection;
    private final ListeningSupport listeningSupport;

    public MessageHandler(Connection connection, ListeningSupport listeningSupport) {
        this.connection = connection;
        this.listeningSupport = listeningSupport;

        startReceiver(connection);
    }

    private void startReceiver(Connection connection) {
        CompletableFuture.runAsync(() -> {
            while (!receiver.isShutdown()) {
                try {
                    Message message = connection.receive();
                    switch (message.getType()) {
                        case RESPONSE:
                            processResponse(message);
                            break;
                        case EVENT:
                            processEvent(message);
                            break;
                    }
                } catch (IOException e) {
                    listeningSupport.notifyListeners(new ClientErrorEvent("Connection error"));
                    shutdown();
                    return;
                }
            }
        }, receiver);
    }

    private void processResponse(Message response) {
        UUID requestId = response.getRequestId();
        CompletableFuture<Message> futureResponse = requests.remove(requestId);
        futureResponse.complete(response);
    }

    private void processEvent(Message event) {
        switch (event.getSubtype()) {
            case LOGIN: {
                String userName = event.getUserName();
                listeningSupport.notifyListeners(new LoginEvent(userName));
                break;
            }
            case NEW_MESSAGE: {
                String userName = event.getUserName();
                String messageContent = event.getMessageContent();
                listeningSupport.notifyListeners(new NewMessageEvent(userName, messageContent));
                break;
            }
            case LOGOUT: {
                String userName = event.getUserName();
                listeningSupport.notifyListeners(new LogoutEvent(userName));
                break;
            }
        }
    }

    public Message sendRequest(Message request)
        throws ExecutionException, InterruptedException {
        CompletableFuture<Message> futureResponse = new CompletableFuture<>();
        requests.put(request.getId(), futureResponse);

        CompletableFuture.runAsync(() -> {
            try {
                connection.send(request);
            } catch (IOException e) {
                futureResponse.completeExceptionally(e);
            }
        }, sender);

        return futureResponse.get();
    }

    public void shutdown() {
        sender.shutdown();
        receiver.shutdown();

        try {
            connection.close();
        } catch (IOException e) {
            listeningSupport.notifyListeners(new ClientErrorEvent("Connection closing error"));
        }
    }
}
