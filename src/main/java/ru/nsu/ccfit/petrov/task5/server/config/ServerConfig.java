package ru.nsu.ccfit.petrov.task5.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import ru.nsu.ccfit.petrov.task5.dto.DTOFormat;

public class ServerConfig {

    private static final String CONFIG_FILE_NAME = "server.properties";
    private static Properties properties;

    private ServerConfig() {
        throw new IllegalStateException("Utility class");
    }

    private static void readConfigFile()
        throws IOException {
        InputStream configFile = ServerConfig.class.getClassLoader()
                                                   .getResourceAsStream(CONFIG_FILE_NAME);
        properties = new Properties();
        properties.load(configFile);
    }

    public static InetAddress getAddress()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

        return InetAddress.getByName(properties.getProperty("connection.address"));
    }

    public static int getPort()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

        return Integer.parseInt(properties.getProperty("connection.port"));
    }

    public static int getTimeout()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

        return Integer.parseInt(properties.getProperty("connection.timeout"));
    }

    public static DTOFormat getDTOFormat()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

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
