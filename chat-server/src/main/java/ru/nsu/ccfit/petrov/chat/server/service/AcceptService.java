package ru.nsu.ccfit.petrov.chat.server.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.connection.ConnectionFactory;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

@Log4j2
@RequiredArgsConstructor
public class AcceptService {

    private final ExecutorService acceptor = Executors.newSingleThreadExecutor();
    private final ServerSocket serverSocket;
    private final int timeout;
    private final DTOFormat dtoFormat;
    private final RegisterService registerService;

    public void start() {
        log.debug("Accept service started");
        acceptor.execute(new AcceptTask());
    }

    public void shutdown() {
        acceptor.shutdown();
    }

    private class AcceptTask
        implements Runnable {

        @Override
        public void run() {
            while (!acceptor.isShutdown()) {
                Socket clientSocket = accept();
                if (clientSocket == null) {
                    continue;
                }
                log.info(String.format("Client %s accepted", clientSocket));

                if (setTimeout(clientSocket)) {
                    continue;
                }

                Connection connection = createConnection(clientSocket);
                if (connection == null) {
                    continue;
                }

                registerService.register(connection);
            }
        }

        private Socket accept() {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                log.error("Failed to accept client connection");
                log.catching(Level.ERROR, e);
                return null;
            }

            return clientSocket;
        }

        private boolean setTimeout(Socket clientSocket) {
            try {
                clientSocket.setSoTimeout(timeout);
            } catch (IOException e) {
                log.error("Failed to set socket timeout");
                log.catching(Level.ERROR, e);
                closeResource(clientSocket);
                return true;
            }

            return false;
        }

        private Connection createConnection(Socket clientSocket) {
            Connection connection = ConnectionFactory.newConnection(dtoFormat);
            try {
                connection.connect(clientSocket);
            } catch (IOException e) {
                log.error("Failed to create read/write connection");
                log.catching(Level.ERROR, e);
                closeResource(connection);
                return null;
            }
            
            return connection;
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
