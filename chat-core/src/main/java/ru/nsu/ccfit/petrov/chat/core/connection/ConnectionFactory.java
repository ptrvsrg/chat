package ru.nsu.ccfit.petrov.chat.core.connection;

import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

public class ConnectionFactory {

    private ConnectionFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection newConnection(DTOFormat dtoFormat) {
        switch (dtoFormat) {
            case JAVA_OBJECT:
                return new JavaObjectConnection();
            case XML_FILE:
                return new XmlFileConnection();
            default:
                throw new IllegalArgumentException("Unsupported DTO format");
        }
    }
}
