package ru.nsu.ccfit.petrov.task5.server.session;

import java.io.Closeable;
import java.util.UUID;
import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.connection.event.ConnectionEvent;
import ru.nsu.ccfit.petrov.task5.connection.event.ConnectionEvent.ConnectionEventType;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.listener.Event;
import ru.nsu.ccfit.petrov.task5.server.event.ServerEvent;
import ru.nsu.ccfit.petrov.task5.server.event.ServerEvent.ServerEventType;
import ru.nsu.ccfit.petrov.task5.server.event.SessionTimeoutEvent;

/**
 * The type {@code Session} is class that describes client session, starts and resets stopwatch, notifies about session
 * timeout.
 *
 * @author ptrvsrg
 */
public class Session
    implements Listener, Closeable {

    @Getter private final UUID id = UUID.randomUUID();
    @Getter private final String userName;
    @Getter private final Connection connection;
    private final Stopwatch stopwatch;
    private final ListeningSupport listeningSupport = new ListeningSupport();

    /**
     * Instantiates a new {@code Session}.
     *
     * @param userName   the username
     * @param connection the connection
     * @param timeout    the timeout
     */
    public Session(String userName, Connection connection, int timeout) {
        this.userName = userName;
        this.connection = connection;
        this.stopwatch = new Stopwatch(timeout);

        stopwatch.start();

        // Notify about StopwatchFinishedEvent to send SessionTimeoutEvent
        stopwatch.addListener(this);

        // Notify about MessageReceivedEvent to reset stopwatch
        connection.addListener(this);
    }

    /**
     * Adds listener.
     *
     * @param listener the listener
     */
    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    @Override
    public void close() {
        stopwatch.stop();
    }

    @Override
    public void processEvent(Event event) {
        if (event instanceof ServerEvent) {
            processServerEvent((ServerEvent) event);
        }

        if (event instanceof ConnectionEvent) {
            processConnectionEvent((ConnectionEvent) event);
        }
    }

    private void processServerEvent(ServerEvent event) {
        if (event.getType() == ServerEventType.STOPWATCH_FINISHED) {
            listeningSupport.notifyListeners(new SessionTimeoutEvent(Session.this));
        }
    }

    private void processConnectionEvent(ConnectionEvent event) {
        if (event.getType() == ConnectionEventType.MESSAGE_RECEIVED) {
            stopwatch.reset();
        }
    }
}