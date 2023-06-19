package ru.nsu.ccfit.petrov.task5.client.controller.handler;

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
import ru.nsu.ccfit.petrov.task5.dto.DTO;

public class MessageHandler {

    private static final int SENDER_COUNT = 3;
    private static final int RECEIVER_COUNT = 3;
    private final Map<UUID, CompletableFuture<DTO>> requests = new ConcurrentHashMap<>();
    private final ExecutorService senders = Executors.newFixedThreadPool(SENDER_COUNT);
    private final ExecutorService receivers = Executors.newFixedThreadPool(RECEIVER_COUNT);
    private final Connection connection;
    private final ListeningSupport listeningSupport;

    public MessageHandler(Connection connection, ListeningSupport listeningSupport) {
        this.connection = connection;
        this.listeningSupport = listeningSupport;

        startReceiver(connection);
    }

    private void startReceiver(Connection connection) {
        CompletableFuture.runAsync(() -> {
            while (!receivers.isShutdown()) {
                try {
                    DTO dto = connection.receive();
                    switch (dto.getType()) {
                        case RESPONSE:
                            processResponse(dto);
                            break;
                        case EVENT:
                            processEvent(dto);
                            break;
                    }
                } catch (IOException e) {
                    closeClient("Connection error", true);
                    return;
                }
            }
        }, receivers);
    }

    private void processResponse(DTO response) {
        UUID requestId = response.getRequestId();
        CompletableFuture<DTO> futureResponse = requests.remove(requestId);
        futureResponse.complete(response);
    }

    private void processEvent(DTO event) {
        switch (event.getSubtype()) {
            case LOGIN: {
                String userName = event.getUsername();
                listeningSupport.notifyListeners(new LoginEvent(userName));
                break;
            }
            case NEW_MESSAGE: {
                String userName = event.getUsername();
                String messageContent = event.getMessage();
                listeningSupport.notifyListeners(new NewMessageEvent(userName, messageContent));
                break;
            }
            case LOGOUT: {
                String userName = event.getUsername();
                listeningSupport.notifyListeners(new LogoutEvent(userName));
                break;
            }
        }
    }

    public DTO sendRequest(DTO request)
        throws ExecutionException, InterruptedException {
        CompletableFuture<DTO> futureResponse = new CompletableFuture<>();
        requests.put(request.getId(), futureResponse);

        CompletableFuture.runAsync(() -> {
            try {
                connection.send(request);
            } catch (IOException e) {
                futureResponse.completeExceptionally(e);
            }
        }, senders);

        return futureResponse.get();
    }

    public void shutdown() {
        senders.shutdown();
        receivers.shutdown();
        connection.close();
    }

    private void closeClient(String reason, boolean terminated) {
        listeningSupport.notifyListeners(new ClientErrorEvent(reason, terminated));
        if (terminated) {
            shutdown();
        }
    }
}
