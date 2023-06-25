package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

public class ConnectionFactory {

    private ConnectionFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection newConnection(DTOFormat dtoFormat, Socket clientSocket)
        throws IOException {
        switch (dtoFormat) {
            case JAVA_OBJECT:
                return new JavaObjectConnection(
                    new ObjectInputStream(clientSocket.getInputStream()),
                    new ObjectOutputStream(clientSocket.getOutputStream()));
            case XML_FILE:
                return new XmlFileConnection(
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream())),
                    new PrintWriter(clientSocket.getOutputStream(), true));
            default:
                throw new IllegalArgumentException("Unsupported DTO format");
        }
    }
}
