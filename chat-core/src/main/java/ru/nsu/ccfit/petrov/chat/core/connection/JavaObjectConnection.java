package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

@RequiredArgsConstructor
public class JavaObjectConnection
    implements Connection {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @Override
    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void connect(Socket clientSocket)
        throws IOException {
        this.clientSocket = clientSocket;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void send(DTO dto)
        throws IOException {
        if (out == null) {
            throw new IOException("Connection not established");
        }

        synchronized (out) {
            out.writeObject(dto);
        }
    }

    @Override
    public DTO receive()
        throws IOException {
        if (in == null) {
            throw new IOException("Connection not established");
        }

        try {
            synchronized (in) {
                return (DTO) in.readObject();
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Invalid DTO format : " + e);
        }
    }

    @Override
    public void close()
        throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}