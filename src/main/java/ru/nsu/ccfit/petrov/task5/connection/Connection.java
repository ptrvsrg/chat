package ru.nsu.ccfit.petrov.task5.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.connection.event.DisconnectEvent;
import ru.nsu.ccfit.petrov.task5.connection.event.MessageReceivedEvent;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code Connection} is class that contains client socket, sends and receives messages. {@code Connection}
 * Thread constantly receives messages from the socket and sends events
 *
 * @author ptrvsrg
 */
public abstract class Connection
    implements Runnable {

    @Getter private final UUID id = UUID.randomUUID();
    private final Socket clientSocket;
    /**
     * The client socket input stream.
     */
    protected final ObjectInputStream in;
    /**
     * The client socket output stream.
     */
    protected final ObjectOutputStream out;
    private final ListeningSupport listeningSupport = new ListeningSupport();

    /**
     * Instantiates a new Connection.
     *
     * @param clientSocket the client socket
     * @throws IOException if an I/O error occurs when creating the input stream, the socket is closed, the socket is
     *                     not connected, or the socket input has been shutdown using
     *                     {@link java.net.Socket#shutdownInput()}
     */
    protected Connection(Socket clientSocket)
        throws IOException {
        this.clientSocket = clientSocket;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Sends message over socket.
     *
     * @param message the message
     * @return {@code true} - message is sent, {@code false} - sending error is happened or message format is invalid
     */
    public abstract boolean send(Message message);

    /**
     * Receives message through socket.
     *
     * @return the message, or {@code null} - receiving error is happened, or message format is invalid
     */
    public abstract Message receive();

    @Override
    public void run() {
        while (!clientSocket.isClosed() && clientSocket.isConnected()) {
            Message message = receive();
            listeningSupport.notifyListeners(new MessageReceivedEvent(id, message));
        }

        listeningSupport.notifyListeners(new DisconnectEvent(id));
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }
}
