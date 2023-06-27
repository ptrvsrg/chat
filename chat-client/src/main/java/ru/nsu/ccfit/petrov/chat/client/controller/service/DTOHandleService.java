package ru.nsu.ccfit.petrov.chat.client.controller.service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.client.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.chat.client.listener.event.ErrorEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.LoginEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.LogoutEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.NewMessageEvent;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

@RequiredArgsConstructor
public class DTOHandleService {

    private static final int SENDER_COUNT = 3;
    private static final int RECEIVER_COUNT = 3;
    private final ExecutorService senders = Executors.newFixedThreadPool(SENDER_COUNT);
    private final ExecutorService receivers = Executors.newFixedThreadPool(RECEIVER_COUNT);
    private final Map<UUID, CompletableFuture<DTO>> requests = new ConcurrentHashMap<>();
    private final String username;
    private final Connection connection;
    private final ListeningSupport listeningSupport;

    public void handle() {
        CompletableFuture.runAsync(new DTOHandleTask(), receivers);
    }

    public DTO sendRequest(DTO request) {
        CompletableFuture<DTO> futureResponse = new CompletableFuture<>();
        requests.put(request.getId(), futureResponse);

        CompletableFuture.runAsync(() -> {
            try {
                connection.send(request);
            } catch (IOException e) {
                futureResponse.completeExceptionally(e);
            }
        }, senders);

        try {
            return futureResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            listeningSupport.notifyListeners(new ErrorEvent("Server is not available"));
        }

        return null;
    }

    public void shutdown() {
        senders.shutdownNow();
        receivers.shutdownNow();
        connection.close();
    }

    private class DTOHandleTask
        implements Runnable {

        @Override
        public void run() {
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
                    listeningSupport.notifyListeners(new ErrorEvent("Server is not available"));
                    return;
                }
            }
        }

        private void processResponse(DTO response) {
            UUID requestId = response.getRequestId();
            CompletableFuture<DTO> futureResponse = requests.remove(requestId);
            if (futureResponse != null) {
                futureResponse.complete(response);
            }
        }

        private void processEvent(DTO event) {
            switch (event.getSubtype()) {
                case LOGIN:
                    listeningSupport.notifyListeners(new LoginEvent(event.getUsername()));
                    break;
                case NEW_MESSAGE:
                    listeningSupport.notifyListeners(
                        new NewMessageEvent(event.getUsername(),
                                            Objects.equals(event.getUsername(), username),
                                            event.getMessage()));
                    break;
                case LOGOUT:
                    listeningSupport.notifyListeners(new LogoutEvent(event.getUsername()));
                    break;
            }
        }
    }
}
