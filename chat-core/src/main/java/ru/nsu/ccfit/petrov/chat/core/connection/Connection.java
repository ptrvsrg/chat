package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

public interface Connection
    extends Closeable {

    Socket getSocket();

    void connect(Socket clientSocket)
        throws IOException;

    void send(DTO dto)
        throws IOException;

    DTO receive()
        throws IOException;
}