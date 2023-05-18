package ru.nsu.ccfit.petrov.task5.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.message.MessageFormat;

@Log4j2
public class ServerConfig {

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

    public static int getPort() {
        return Integer.parseInt(properties.getProperty("connection.port"));
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout"));
    }

    public static MessageFormat getMessageFormat() {
        String messageFormat = properties.getProperty("message.format");

        switch (messageFormat) {
            case "java_object":
                return MessageFormat.JAVA_OBJECT;
            case "xml_file":
                return MessageFormat.XML_FILE;
            default:
                throw new IllegalStateException("Unsupported message format");
        }
    }
}