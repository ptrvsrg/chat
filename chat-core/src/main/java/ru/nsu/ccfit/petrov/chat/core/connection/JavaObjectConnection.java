package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

@RequiredArgsConstructor
public class JavaObjectConnection
    implements Connection {

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

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
            throw new IOException("Invalid DTO format : " + e);
        }
    }
}