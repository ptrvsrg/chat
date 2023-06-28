package ru.nsu.ccfit.petrov.chat.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

/**
 * The type ServerConfig is utility class that parses config file and returns available properties.
 *
 * @author ptrvsrg
 */
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

    /**
     * Get server internet address.
     *
     * @return the address
     * @throws IOException If it was not possible to parse the configuration file
     */
    public static InetAddress getAddress()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

        return InetAddress.getByName(properties.getProperty("connection.address"));
    }

    /**
     * Get server internet port.
     *
     * @return the port
     * @throws IOException If it was not possible to parse the configuration file
     */
    public static int getPort()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

        return Integer.parseInt(properties.getProperty("connection.port"));
    }

    /**
     * Get timeout to disconnect client when idle.
     *
     * @return the timeout
     * @throws IOException If it was not possible to parse the configuration file
     */
    public static int getTimeout()
        throws IOException {
        if (properties == null) {
            readConfigFile();
        }

        return Integer.parseInt(properties.getProperty("connection.timeout"));
    }

    /**
     * Get DTO format.
     *
     * @return the DTO format
     * @throws  IOException If it was not possible to parse the configuration file
     * @throws IllegalStateException If parsed DTO format is unsupported
     */
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
