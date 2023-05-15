package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;
import ru.nsu.ccfit.petrov.task5.server.manager.ClientManager;

@Log4j2
public class Server {

    private final ClientManager clientManager = new ClientManager();
    private ServerSocket serverSocket;

    public void start() {
        try {
            serverSocket = new ServerSocket(ServerConfig.getPort());
        } catch (IOException e) {
            log.info("Server didn't start");
            log.catching(Level.INFO, e);
            return;
        }

        acceptClient();
    }

    public void acceptClient() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(ServerConfig.getTimeout());

                Connection connection = new Connection(clientSocket);
                clientManager.addClient(connection);
            } catch (IOException e) {
                log.info("Connection lost");
                log.catching(Level.INFO, e);
                break;
            }
        }
    }
}