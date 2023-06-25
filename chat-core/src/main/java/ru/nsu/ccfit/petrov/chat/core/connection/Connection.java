package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.IOException;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

public interface Connection {

    void send(DTO dto)
        throws IOException;

    DTO receive()
        throws IOException;
}