package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

/**
 * The interface Connection is interface that describes the functionality of connecting via
 * read/write sockets using various types of DTO.
 *
 * @author ptrvsrg
 */
public interface Connection
    extends Closeable {

    /**
     * Gets socket.
     *
     * @return the socket
     */
    Socket getSocket();

    /**
     * Initialize streams for read/write using connected socket.
     *
     * @param clientSocket the client socket
     * @throws IOException If an I/O error occurs
     */
    void connect(Socket clientSocket)
        throws IOException;

    /**
     * Send DTO.
     *
     * @param dto the DTO
     * @throws IOException If connection not established or DTO format is invalid
     */
    void send(DTO dto)
        throws IOException;

    /**
     * Receive DTO.
     *
     * @return the DTO
     * @throws IOException If connection not established or DTO format is invalid
     */
    DTO receive()
        throws IOException;
}