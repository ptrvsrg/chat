package ru.nsu.ccfit.petrov.task5.connection;

import java.io.IOException;
import java.net.Socket;
import lombok.extern.log4j.Log4j2;
import ru.nsu.ccfit.petrov.task5.connection.object.JavaObjectConnection;
import ru.nsu.ccfit.petrov.task5.connection.xml.XmlConnection;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig.MessageFormat;

/**
 * The type {@code ConnectionFactory} is class that instantiates new {@link Connection} by message format.
 *
 * @author ptrvsrg
 */
@Log4j2
public class ConnectionFactory {

    private ConnectionFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates connection by message format.
     *
     * @param messageType  the message type
     * @param clientSocket the client socket
     * @return the connection, or {@code null} - message format is invalid
     * @throws IOException if an I/O error occurs when creating the input stream, the socket is closed, the socket is
     *                     not connected, or the socket input has been shutdown using
     *                     {@link java.net.Socket#shutdownInput()}
     */
    public static Connection create(MessageFormat messageType, Socket clientSocket)
        throws IOException {
        switch (messageType) {
            case JAVA_OBJECT:
                return new JavaObjectConnection(clientSocket);
            case XML:
                return new XmlConnection(clientSocket);
            default:
                return null;
        }
    }
}
