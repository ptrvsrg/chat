package ru.nsu.ccfit.petrov.task5.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

/**
 * The type {@code ServerConfig} is utility class that parses server configuration file.
 *
 * @author ptrvsrg
 */
@Log4j2
public class ServerConfig {

    /**
     * The type {@code MessageFormat} is enum that describes message formats.
     */
    public enum MessageFormat {
        /**
         * Java object message format.
         */
        JAVA_OBJECT,
        /**
         * Xml message format.
         */
        XML
    }

    private static final String CONFIG_FILE_NAME = "server.properties";
    private static final Properties properties = new Properties();

    private ServerConfig() {
        throw new IllegalStateException("Utility class");
    }

    static {
        try (InputStream configFile = ServerConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(configFile);
        } catch (IOException e) {
            log.catching(Level.FATAL, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public static int getPort() {
        return Integer.parseInt(properties.getProperty("connection.port"));
    }

    /**
     * Gets session timeout.
     *
     * @return the timeout
     */
    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("session.timeout"));
    }

    /**
     * Gets message format.
     *
     * @return the message format
     */
    public static MessageFormat getMessageFormat() {
        String messageFormat = properties.getProperty("message.format");

        switch (messageFormat) {
            case "object":
                return MessageFormat.JAVA_OBJECT;
            case "xml":
                return MessageFormat.XML;
            default:
                throw new IllegalStateException("Unsupported message format");
        }
    }
}
