package ru.nsu.ccfit.petrov.chat.client.listener;

import java.util.EventListener;
import ru.nsu.ccfit.petrov.chat.client.listener.event.Event;

/**
 * The type Listener is interface that describes the behavior of listeners for objects that is containing
 * {@link ListeningSupport}.
 *
 * @author ptrvsrg
 */
public interface Listener
    extends EventListener {

    /**
     * Process event.
     *
     * @param event the event
     */
    void processEvent(Event event);
}
