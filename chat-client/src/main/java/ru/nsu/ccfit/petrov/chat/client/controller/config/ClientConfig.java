package ru.nsu.ccfit.petrov.chat.client.controller.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

/**
 * The type ClientConfig is utility class that parses config file and returns available properties.
 *
 * @author ptrvsrg
 */
public class ClientConfig {

    private static final String CONFIG_FILE_NAME = "client.properties";
    private static Properties properties;

    private ClientConfig() {
        throw new IllegalStateException("Utility class");
    }

    private static void readConfigFile()
        throws IOException {
        InputStream configFile = ClientConfig.class.getClassLoader()
                                                   .getResourceAsStream(CONFIG_FILE_NAME);
        properties = new Properties();
        properties.load(configFile);
    }

    /**
     * Gets DTO format.
     *
     * @return the DTO format
     * @throws IOException           If it was not possible to parse the configuration file
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
