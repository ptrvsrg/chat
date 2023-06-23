package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.IOException;
import java.net.Socket;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

public class ConnectionFactory {

    private ConnectionFactory() {
        throw new IllegalStateException("Utility class");
    }
    
    public static Connection newConnection(DTOFormat dtoFormat, Socket clientSocket)
        throws IOException {
        switch (dtoFormat) {
            case JAVA_OBJECT:
                return new JavaObjectConnection(clientSocket);
            case XML_FILE:
                return new XmlFileConnection(clientSocket);
            default:
                throw new IllegalStateException("Unsupported connection format");
        }
    }
}
