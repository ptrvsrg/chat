package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;
import ru.nsu.ccfit.petrov.task5.listener.event.SessionTimeoutEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.processor.AbstractEventProcessor;
import ru.nsu.ccfit.petrov.task5.listener.event.processor.EventProcessor;

@Log4j2
public abstract class AbstractServer
    implements Listener {

    private final ServerSocket serverSocket;
    private final int timeout;
    private final List<ClientSession> clientSessions = new ArrayList<>();

    protected AbstractServer(int port, int timeout) {
        this.timeout = timeout;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            log.catching(Level.FATAL, e);
            throw new RuntimeException(e);
        }
    }

    public void start() {
        while (true) {
            try (serverSocket) {
                Socket clientSocket = serverSocket.accept();
                addClient(clientSocket);
            } catch (IOException e) {
                log.catching(Level.FATAL, e);
            }
        }
    }

    public void stop() {
        removeAllClients();
    }

    protected abstract AbstractRequestHandler createMessageHandler(Socket clientSocket);

    private void addClient(Socket clientSocket) {
        AbstractRequestHandler messageHandler = createMessageHandler(clientSocket);
        ClientSession clientSession = new ClientSession(messageHandler, timeout);
        clientSession.start();
        clientSession.addListener(this);
        clientSessions.add(clientSession);
    }

    private void removeClient(ClientSession clientSession) {
        clientSession.stop();
        clientSessions.remove(clientSession);
    }

    private void removeAllClients() {
        for (ClientSession clientSession : clientSessions) {
            removeClient(clientSession);
        }
    }

    /**
     * Handles the context of the {@link ru.nsu.ccfit.petrov.task5.listener.ListeningSupport Observable} object message.
     *
     * @param event the context
     */
    @Override
    public void processEvent(Event event) {
        EventProcessor eventProcessor = new AbstractEventProcessor() {
            @Override
            public void processEvent(SessionTimeoutEvent event) {
                ClientSession clientSession = (ClientSession) event.getSource();
                removeClient(clientSession);
            }
        };

        event.process(eventProcessor);
    }
}
