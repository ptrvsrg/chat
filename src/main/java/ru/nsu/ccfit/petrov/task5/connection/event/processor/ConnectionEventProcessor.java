package ru.nsu.ccfit.petrov.task5.connection.event.processor;

import ru.nsu.ccfit.petrov.task5.connection.event.DisconnectEvent;
import ru.nsu.ccfit.petrov.task5.connection.event.MessageReceivedEvent;

/**
 * The type {@code ConnectionEventProcessor} is interface that describes behavior objects that processes
 * {@link ru.nsu.ccfit.petrov.task5.connection.event.ConnectionEvent ConnectionEvent}.
 *
 * @author ptrvsrg
 */
public interface ConnectionEventProcessor {

    /**
     * Processes disconnection event.
     *
     * @param event the event
     */
    void processEvent(DisconnectEvent event);

    /**
     * Processes message received event.
     *
     * @param event the event
     */
    void processEvent(MessageReceivedEvent event);
}
