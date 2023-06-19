package ru.nsu.ccfit.petrov.task5.connection;

import java.io.IOException;
import java.net.Socket;
import ru.nsu.ccfit.petrov.task5.dto.DTO;

public class JavaObjectConnection
    extends Connection {

    public JavaObjectConnection(Socket clientSocket)
        throws IOException {
        super(clientSocket);
    }

    @Override
    public void send(DTO dto)
        throws IOException {
        synchronized (out) {
            out.writeObject(dto);
        }
    }

    @Override
    public DTO receive()
        throws IOException {
        try {
            synchronized (in) {
                return (DTO) in.readObject();
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Invalid message format : " + e);
        }
    }
}