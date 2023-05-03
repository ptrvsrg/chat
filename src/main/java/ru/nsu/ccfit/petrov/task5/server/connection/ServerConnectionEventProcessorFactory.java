package ru.nsu.ccfit.petrov.task5.server.connection;

import java.util.Map;
import java.util.UUID;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig.MessageFormat;
import ru.nsu.ccfit.petrov.task5.server.connection.object.JavaObjectServerConnectionEventProcessor;
import ru.nsu.ccfit.petrov.task5.server.connection.xml.XmlServerConnectionEventProcessor;

/**
 * The type {@code ServerConnectionEventProcessorFactory} is class that instantiates new
 * {@link ServerConnectionEventProcessor} by message format.
 *
 * @author ptrvsrg
 */
public class ServerConnectionEventProcessorFactory {

    private ServerConnectionEventProcessorFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create server connection event processor.
     *
     * @param messageFormat the message format
     * @param connections   the connections
     * @return the server connection event processor
     */
    public static ServerConnectionEventProcessor create(MessageFormat messageFormat,
                                                        Map<UUID, Connection> connections) {
        switch (messageFormat) {
            case JAVA_OBJECT:
                return new JavaObjectServerConnectionEventProcessor(connections);
            case XML:
                return new XmlServerConnectionEventProcessor(connections);
            default:
                return null;
        }
    }
}
