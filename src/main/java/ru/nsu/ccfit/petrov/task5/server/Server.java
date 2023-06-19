package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.connection.ConnectionFactory;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;
import ru.nsu.ccfit.petrov.task5.server.manager.ClientManager;

@Log4j2
public class Server {

    private final ClientManager clientManager = new ClientManager();
    private ServerSocket serverSocket;

    public void start() {
        try {
            serverSocket = new ServerSocket(ServerConfig.getPort());
            log.info("Server started");
        } catch (IOException e) {
            log.error("Server didn't start");
            log.catching(Level.ERROR, e);
            return;
        }

        acceptClient();
    }

    public void acceptClient() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                log.info(String.format("Client %s accepted", clientSocket));

                clientSocket.setSoTimeout(ServerConfig.getTimeout());
                Connection connection = ConnectionFactory.newConnection(clientSocket);
                clientManager.addClient(connection);
                log.info(String.format("Client %s added", clientSocket));
            } catch (IOException e) {
                log.info("Connection lost");
                log.catching(Level.INFO, e);
                break;
            }
        }
    }
}