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

/**
 * The type DTOHandleService is class that describes service for sending requests and handling
 * responses and events.
 *
 * @author ptrvsrg
 */
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

    /**
     * Start handling responses and events.
     */
    public void handle() {
        CompletableFuture.runAsync(new DTOHandleTask(), receivers);
    }

    /**
     * Send request.
     *
     * @param request the request
     * @return the response
     */
    public DTO sendRequest(DTO request) {
        CompletableFuture<DTO> futureResponse = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                connection.send(request);
            } catch (IOException e) {
                futureResponse.completeExceptionally(e);
            }
        }, senders);
        requests.put(request.getId(), futureResponse);

        try {
            return futureResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            listeningSupport.notifyListeners(new ErrorEvent("Server is not available"));
        }

        return null;
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        senders.shutdownNow();
        receivers.shutdownNow();
        try {
            connection.close();
        } catch (IOException ignored) {
        }
    }

    private class DTOHandleTask
        implements Runnable {

        @Override
        public void run() {
            while (!receivers.isShutdown()) {
                DTO dto;
                try {
                    dto = connection.receive();
                } catch (IOException e) {
                    listeningSupport.notifyListeners(new ErrorEvent("Server is not available"));
                    return;
                }

                if (DTO.isResponse(dto)) {
                    processResponse(dto);
                } else if (DTO.isEvent(dto)) {
                    processEvent(dto);
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
            if (DTO.isLoginEvent(event)) {
                listeningSupport.notifyListeners(new LoginEvent(event.getUsername()));
            } else if (DTO.isNewMessageEvent(event)) {
                boolean isCurrentUser = Objects.equals(event.getUsername(), username);
                listeningSupport.notifyListeners(
                    new NewMessageEvent(event.getUsername(), isCurrentUser, event.getMessage()));
            } else if (DTO.isLogoutEvent(event)) {
                listeningSupport.notifyListeners(new LogoutEvent(event.getUsername()));
            }
        }
    }
}