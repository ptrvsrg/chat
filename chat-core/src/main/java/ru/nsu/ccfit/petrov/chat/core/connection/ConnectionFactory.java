package ru.nsu.ccfit.petrov.chat.core.connection;

import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

/**
 * The type ConnectionFactory is utility class that has factory method for instantiation
 * {@link Connection} objects by DTO format.
 *
 * @author ptrvsrg
 */
public class ConnectionFactory {

    private ConnectionFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Instantiate {@link Connection} objects by DTO format.
     *
     * @param dtoFormat the DTO format
     * @return the connection
     * @throws IllegalArgumentException If DTO format is unsupported
     */
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
