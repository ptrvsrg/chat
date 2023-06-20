package ru.nsu.ccfit.petrov.task5.server.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.connection.ConnectionFactory;
import ru.nsu.ccfit.petrov.task5.dto.DTOFormat;

@Log4j2
@RequiredArgsConstructor
public class AcceptService {

    private final ExecutorService acceptor = Executors.newSingleThreadExecutor();
    private final ServerSocket serverSocket;
    private final int timeout;
    private final DTOFormat dtoFormat;
    private final RegisterService registerService;

    public void start() {
        acceptor.execute(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(timeout);
                    Connection connection = ConnectionFactory.newConnection(dtoFormat,
                                                                            clientSocket);
                    registerService.register(connection);
                } catch (IOException e) {
                    log.info("Connection lost");
                    log.catching(Level.INFO, e);
                    break;
                }
            }
        });
    }
}
