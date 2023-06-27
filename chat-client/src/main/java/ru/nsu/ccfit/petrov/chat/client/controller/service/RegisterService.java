package ru.nsu.ccfit.petrov.chat.client.controller.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.client.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.chat.client.listener.event.ErrorEvent;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

@RequiredArgsConstructor
public class RegisterService {

    private final ExecutorService registrar = Executors.newSingleThreadExecutor();
    private final ListeningSupport listeningSupport;

    public boolean register(Connection connection, String username) {
        Future<DTO> futureResponse = registrar.submit(new RegisterTask(connection, username));

        DTO response;
        try {
            response = futureResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            listeningSupport.notifyListeners(new ErrorEvent("Connection error"));
            return false;
        }

        if (DTO.isErrorResponse(response)) {
            listeningSupport.notifyListeners(new ErrorEvent(response.getMessage()));
            return false;
        }

        return true;
    }

    public void shutdown() {
        registrar.shutdownNow();
    }

    @RequiredArgsConstructor
    private class RegisterTask
        implements Callable<DTO> {

        private final Connection connection;
        private final String username;

        @Override
        public DTO call()
            throws Exception {
            DTO request = DTO.newLoginRequest(username);
            connection.send(request);

            while (!registrar.isShutdown()) {
                DTO response = connection.receive();

                if (DTO.isResponse(response)) {
                    return response;
                }
            }

            return null;
        }
    }
}
