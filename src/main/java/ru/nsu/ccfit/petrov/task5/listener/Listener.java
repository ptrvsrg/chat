package ru.nsu.ccfit.petrov.task5.listener;

import java.util.EventListener;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;

/**
 * The type {@code Observer} is interface describing the behavior of listeners for objects of type
 * {@link ListeningSupport Observable}.
 *
 * @author ptrvsrg
 */
public interface Listener
    extends EventListener {

    /**
     * Handles the context of the {@link ListeningSupport Observable} object message.
     *
     * @param event the context
     */
    void processEvent(Event event);
}
