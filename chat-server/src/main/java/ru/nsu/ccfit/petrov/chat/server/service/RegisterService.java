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

/**
 * The type RegisterService is class that describes service for registration and forwarding for
 * handling requests.
 *
 * @author ptrvsrg
 */
@Log4j2
@RequiredArgsConstructor
public class RegisterService {

    private static final int REGISTRAR_COUNT = 2;
    private final ExecutorService registrars = Executors.newFixedThreadPool(REGISTRAR_COUNT);
    private final UserRepository userRepository;
    private final RequestHandleService requestHandleService;

    /**
     * Register client by connection.
     *
     * @param connection the connection
     */
    public void register(Connection connection) {
        registrars.execute(new RegisterTask(connection));
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        registrars.shutdown();
    }

    @RequiredArgsConstructor
    private class RegisterTask
        implements Runnable {

        private final Connection connection;

        @Override
        public void run() {
            while (!registrars.isShutdown()) {
                DTO request = receiveRequest();
                if (request == null) {
                    closeConnection(connection);
                    return;
                }

                DTO response = createResponse(request);
                if (!sendResponse(response)) {
                    closeConnection(connection);
                    return;
                }

                if (DTO.isSuccessResponse(response)) {
                    userRepository.addUser(new User(request.getUsername(), connection));
                    sendEvent(DTO.newLoginEvent(request.getUsername()));
                    log.info(String.format("Client %s is registered", connection.getSocket()
                                                                                .getRemoteSocketAddress()));
                    requestHandleService.handle(connection);
                    return;
                }
            }
        }

        private DTO receiveRequest() {
            DTO request;
            try {
                request = connection.receive();
            } catch (IOException e) {
                log.info("Client not registered");
                log.error("Failed to receive request");
                log.catching(Level.ERROR, e);
                return null;
            }

            return request;
        }

        private DTO createResponse(DTO request) {
            if (!DTO.isLoginRequest(request)) {
                return DTO.newErrorResponse(request.getId(), "DTO is not request");
            } else if (!usernameIsCorrect(request)) {
                return DTO.newErrorResponse(request.getId(), "Username format is invalid");
            } else if (usernameIsUsed(request)) {
                return DTO.newErrorResponse(request.getId(), "Username already used");
            }

            return DTO.newSuccessResponse(request.getId());
        }

        private boolean sendResponse(DTO response) {
            try {
                connection.send(response);
                return true;
            } catch (IOException e) {
                log.info("Client not registered");
                log.error("Failed to send response");
                log.catching(Level.ERROR, e);
                return false;
            }
        }

        private boolean usernameIsCorrect(DTO request) {
            return request.getUsername() != null && !request.getUsername()
                                                            .isEmpty();
        }

        private boolean usernameIsUsed(DTO request) {
            return userRepository.findUserByUsername(request.getUsername()) != null;
        }

        private void sendEvent(DTO event) {
            for (Connection userConnection : userRepository.getConnections()) {
                try {
                    userConnection.send(event);
                } catch (IOException e) {
                    log.error("Failed to send event");
                    log.catching(Level.ERROR, e);
                }
            }
        }

        private void closeConnection(Connection connection) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("Failed to close resource");
                log.catching(Level.ERROR, e);
            }
        }
    }
}
