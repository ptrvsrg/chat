package ru.nsu.ccfit.petrov.task5.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.dto.DTOFormat;

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

    public static DTOFormat getDTOFormat() {
        String dtoFormat = properties.getProperty("DTO.format");

        switch (dtoFormat) {
            case "java_object":
                return DTOFormat.JAVA_OBJECT;
            case "xml_file":
                return DTOFormat.XML_FILE;
            default:
                throw new IllegalStateException("Unsupported DTO format");
        }
    }
}
