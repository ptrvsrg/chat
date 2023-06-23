package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

public abstract class Connection
    implements Closeable {

    private final Socket clientSocket;
    protected final ObjectInputStream in;
    protected final ObjectOutputStream out;

    protected Connection(Socket clientSocket)
        throws IOException {
        this.clientSocket = clientSocket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public abstract void send(DTO dto)
        throws IOException;

    public abstract DTO receive()
        throws IOException;

    @Override
    public void close() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException ignored) {}
    }
}