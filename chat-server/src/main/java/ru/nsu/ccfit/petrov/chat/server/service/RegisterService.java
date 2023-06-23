package ru.nsu.ccfit.petrov.chat.server.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Subtype;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Type;
import ru.nsu.ccfit.petrov.chat.server.database.User;
import ru.nsu.ccfit.petrov.chat.server.database.UserRepository;

@Log4j2
@RequiredArgsConstructor
public class RegisterService {

    private static final int REGISTRAR_COUNT = 2;
    private final ExecutorService registrars = Executors.newFixedThreadPool(REGISTRAR_COUNT);
    private final UserRepository userRepository;
    private final RequestHandleService requestHandleService;

    public void register(Connection connection) {
        registrars.execute(new RegisterTask(connection));
    }

    public void shutdown() {
        registrars.shutdownNow();
    }

    @RequiredArgsConstructor
    private class RegisterTask
        implements Runnable {

        private final Connection connection;

        @Override
        public void run() {
            while (true) {
                try {
                    DTO request = connection.receive();

                    if (!isRequest(request)) {
                        continue;
                    }
                    if (!usernameIsCorrect(request)) {
                        connection.send(
                            DTO.newErrorResponse(request.getId(), "Username format is invalid"));
                        continue;
                    }
                    if (usernameIsUsed(request)) {
                        connection.send(
                            DTO.newErrorResponse(request.getId(), "Username already used"));
                        continue;
                    }

                    userRepository.addUser(new User(request.getUsername(), connection));
                    connection.send(DTO.newSuccessResponse(request.getId()));
                    sendEvent(DTO.newLoginEvent(request.getUsername()));
                    break;
                } catch (IOException e) {
                    log.info("Client didn't add");
                    log.catching(Level.INFO, e);
                    connection.close();
                    return;
                }
            }

            requestHandleService.handleRequests(connection);
        }

        private boolean isRequest(DTO request) {
            return request != null && request.getType() == Type.REQUEST &&
                   request.getSubtype() == Subtype.LOGIN;
        }

        private boolean usernameIsCorrect(DTO request) {
            return request.getUsername() != null && !request.getUsername()
                                                            .isEmpty();
        }

        private boolean usernameIsUsed(DTO request) {
            return userRepository.findUserByUsername(request.getUsername()) != null;
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
    }
}
