package ru.nsu.ccfit.petrov.task5.connection;

import java.io.IOException;
import java.net.Socket;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;

public class ConnectionFactory {

    private ConnectionFactory() {
        throw new IllegalStateException("Utility class");
    }
    
    public static Connection newConnection(Socket clientSocket)
        throws IOException {
        switch (ServerConfig.getDTOFormat()) {
            case JAVA_OBJECT:
                return new JavaObjectConnection(clientSocket);
            case XML_FILE:
                return new XmlFileConnection(clientSocket);
            default:
                throw new IllegalStateException("Unsupported connection format");
        }
    }
}
