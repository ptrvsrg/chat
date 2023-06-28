package ru.nsu.ccfit.petrov.chat.client.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import ru.nsu.ccfit.petrov.chat.client.listener.event.Event;

/**
 * The type ListeningSupport is class that stores {@link Listener} and notifies them if its state
 * change.
 *
 * @author ptrvsrg
 */
public class ListeningSupport {

    @Getter private final Set<Listener> listeners = new HashSet<>();

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Add listeners.
     *
     * @param listeners the listeners
     */
    public void addListeners(Collection<? extends Listener> listeners) {
        this.listeners.addAll(listeners);
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify listeners.
     *
     * @param event the event
     */
    public void notifyListeners(Event event) {
        for (Listener listener : listeners) {
            listener.processEvent(event);
        }
    }
}
