package ru.nsu.ccfit.petrov.task5.listener;

import java.util.EventListener;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;

/**
 * The type {@code Listener} is interface that describes behavior of objects that subscribes to receive events from
 * objects with {@link ListeningSupport} and processes its events.
 *
 * @author ptrvsrg
 */
public interface Listener
    extends EventListener {

    /**
     * Processes the event from object with {@link ListeningSupport}.
     *
     * @param event the event
     */
    void processEvent(Event event);
}
