package ru.nsu.ccfit.petrov.task5.server;

import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig.MessageFormat;

public class ServerApplication {

    public static void main(String[] args) {
        int port = ServerConfig.getPort();
        MessageFormat messageFormat = ServerConfig.getMessageFormat();
        int timeout = ServerConfig.getTimeout();

        try {
            Server server = new Server(port, messageFormat, timeout);
            server.start();
        } catch (Exception e) {
            System.exit(1);
        }
    }
}
