package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.net.InetAddress;
import ru.nsu.ccfit.petrov.task5.dto.DTOFormat;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;

public class ServerApp {

    public static void main(String[] args)
        throws IOException {
        InetAddress address = ServerConfig.getAddress();
        int port = ServerConfig.getPort();
        int timeout = ServerConfig.getTimeout();
        DTOFormat dtoFormat = ServerConfig.getDTOFormat();

        Server server = new Server();
        server.start(address, port, timeout, dtoFormat);
    }
}
