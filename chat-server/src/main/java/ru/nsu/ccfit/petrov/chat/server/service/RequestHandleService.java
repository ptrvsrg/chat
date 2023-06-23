package ru.nsu.ccfit.petrov.chat.server.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Type;
import ru.nsu.ccfit.petrov.chat.server.database.User;
import ru.nsu.ccfit.petrov.chat.server.database.UserRepository;

@Log4j2
@RequiredArgsConstructor
public class RequestHandleService {

    private static final int HANDLER_COUNT = 3;
    private final ExecutorService handlers = Executors.newFixedThreadPool(HANDLER_COUNT);
    private final UserRepository userRepository;

    public void handleRequests(Connection connection) {
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

            try {
                DTO request = user.getConnection()
                                  .receive();

                if (!isRequest(request)) {
                    handlers.execute(this);
                    return;
                }

                switch (request.getSubtype()) {
                    case NEW_MESSAGE: {
                        user.getConnection()
                            .send(DTO.newSuccessResponse(request.getId()));
                        sendEvent(DTO.newNewMessageEvent(user.getUsername(), request.getMessage()));
                        break;
                    }
                    case USER_LIST: {
                        String[] usernames = userRepository.getUsernames();
                        user.getConnection()
                            .send(DTO.newSuccessResponse(request.getId(), usernames));
                        break;
                    }
                    case LOGOUT: {
                        user.getConnection()
                            .send(DTO.newSuccessResponse(request.getId()));
                        removeUser();
                        return;
                    }
                }

                handlers.execute(this);
            } catch (IOException e) {
                log.info("Message handling aborted");
                log.catching(Level.INFO, e);
                removeUser();
            }
        }

        private boolean isRequest(DTO request) {
            return request != null && request.getType() == Type.REQUEST;
        }

        private void sendEvent(DTO dto) {
            for (Connection userConnection : userRepository.getConnections()) {
                try {
                    userConnection.send(dto);
                } catch (IOException e) {
                    log.info("Message didn't send");
                    log.catching(Level.INFO, e);
                }
            }
        }

        private void removeUser() {
            if (user == null) {
                return;
            }

            sendEvent(DTO.newLogoutEvent(user.getUsername()));
            userRepository.removeUser(user.getUsername());
            user.getConnection()
                .close();
        }
    }
}
