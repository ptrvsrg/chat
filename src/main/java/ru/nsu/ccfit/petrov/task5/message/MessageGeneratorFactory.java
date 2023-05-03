package ru.nsu.ccfit.petrov.task5.message;

import ru.nsu.ccfit.petrov.task5.message.object.JavaObjectMessageGenerator;
import ru.nsu.ccfit.petrov.task5.message.xml.XmlMessageGenerator;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig.MessageFormat;

/**
 * The type {@code MessageGeneratorFactory} is class that instantiates new {@link MessageGenerator} by message format.
 *
 * @author ptrvsrg
 */
public class MessageGeneratorFactory {

    private MessageGeneratorFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates new {@code MessageGenerator} by message format.
     *
     * @param messageFormat the message format
     * @return the message generator, or {@code null} - message format is invalid
     */
    public static MessageGenerator create(MessageFormat messageFormat) {
        switch (messageFormat) {
            case JAVA_OBJECT:
                return new JavaObjectMessageGenerator();
            case XML:
                return new XmlMessageGenerator();
            default:
                return null;
        }
    }
}
