package ru.nsu.ccfit.petrov.task5.connection.object;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.object.JavaObjectMessage;

/**
 * The type {@code JavaObjectConnection} is class that implements abstract method of {@link Connection} for sending and
 * receiving Java objects.
 *
 * @author ptrvsrg
 */
@Log4j2
public class JavaObjectConnection
    extends Connection {

    /**
     * Instantiates a new JavaObjectConnection.
     *
     * @param socket the socket
     * @throws IOException if an I/O error occurs when creating the input stream, the socket is closed, the socket is
     *                     not connected, or the socket input has been shutdown using
     *                     {@link java.net.Socket#shutdownInput()}
     */
    public JavaObjectConnection(Socket socket)
        throws IOException {
        super(socket);
    }

    @Override
    public boolean send(Message message) {
        if (!Objects.equals(message.getClass(), JavaObjectMessage.class)) {
            log.error("Invalid message format");
            return false;
        }

        try {
            out.writeObject(message);
        } catch (IOException e) {
            log.catching(Level.ERROR, e);
            return false;
        }

        return true;
    }

    @Override
    public Message receive() {
        JavaObjectMessage message = null;
        try {
            message = (JavaObjectMessage) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            log.catching(Level.ERROR, e);
        } catch (ClassCastException e) {
            log.error("Invalid message format");
        }

        return message;
    }
}
