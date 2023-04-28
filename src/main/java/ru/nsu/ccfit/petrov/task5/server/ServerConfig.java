package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

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
        return Integer.parseInt(properties.getProperty("session.timeout"));
    }

    public static String getMessageType() {
        return properties.getProperty("message.type");
    }
}
