package ru.nsu.ccfit.petrov.chat.server.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.server.database.User;
import ru.nsu.ccfit.petrov.chat.server.database.UserRepository;

@Log4j2
@RequiredArgsConstructor
public class RequestHandleService {

    private static final int HANDLER_COUNT = 3;
    private final ExecutorService handlers = Executors.newFixedThreadPool(HANDLER_COUNT);
    private final UserRepository userRepository;

    public void handle(Connection connection) {
        handlers.execute(new RequestHandleTask(connection));
    }

    public void shutdown() {
        handlers.shutdownNow();
    }

    private class RequestHandleTask
        implements Runnable {

        private final User user;

        public RequestHandleTask(Connection connection) {
            this.user = userRepository.findUserByConnection(connection);
        }

        @Override
        public void run() {
            if (user == null) {
                return;
            }

            // Receive request
            DTO request = receiveRequest();
            if (request == null) {
                removeUser();
                return;
            }

            // Send response and execute request actions
            if (!DTO.isRequest(request)) {
                if (!sendResponse(DTO.newErrorResponse(request.getId(), "DTO is not request"))) {
                    removeUser();
                    return;
                }
            } else if (DTO.isNewMessageRequest(request)) {
                if (!sendResponse(DTO.newSuccessResponse(request.getId()))) {
                    removeUser();
                    return;
                }
                sendEvent(DTO.newNewMessageEvent(user.getUsername(), request.getMessage()));
            } else if (DTO.isUserListRequest(request)) {
                if (!sendResponse(
                    DTO.newSuccessResponse(request.getId(), userRepository.getUsernames()))) {
                    removeUser();
                    return;
                }
            } else if (DTO.isLogoutRequest(request)) {
                if (!sendResponse(DTO.newSuccessResponse(request.getId()))) {
                    removeUser();
                    return;
                }
                removeUser();
            }

            handlers.execute(this);
        }

        private DTO receiveRequest() {
            DTO request;
            try {
                request = user.getConnection()
                              .receive();
            } catch (IOException e) {
                log.error("Failed to receive request");
                log.catching(Level.ERROR, e);
                return null;
            }

            return request;
        }

        private boolean sendResponse(DTO response) {
            try {
                user.getConnection()
                    .send(response);
                return true;
            } catch (IOException e) {
                log.error("Failed to send response");
                log.catching(Level.ERROR, e);
                return false;
            }
        }

        private void sendEvent(DTO dto) {
            for (Connection connection : userRepository.getConnections()) {
                try {
                    connection.send(dto);
                } catch (IOException e) {
                    log.error("Failed to send event");
                    log.catching(Level.ERROR, e);
                }
            }
        }

        private void removeUser() {
            if (user == null) {
                return;
            }

            sendEvent(DTO.newLogoutEvent(user.getUsername()));
            userRepository.removeUser(user.getUsername());
            closeResource(user.getConnection());
        }

        private void closeResource(AutoCloseable resource) {
            try {
                resource.close();
            } catch (Exception e) {
                log.error("Failed to close resource");
                log.catching(Level.ERROR, e);
            }
        }
    }
}
