package ru.nsu.ccfit.petrov.chat.server;

import java.io.IOException;
import java.net.InetAddress;
import ru.nsu.ccfit.petrov.chat.server.config.ServerConfig;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

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
