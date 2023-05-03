package ru.nsu.ccfit.petrov.task5.listener;

import java.util.ArrayList;

/**
 * The type {@code Observable} is class that stores
 * {@link Listener listeners} and notifies them if its
 * state change.
 *
 * @author ptrvsrg
 */
public class ListeningSupport {

    private ArrayList<Listener> listeners = null;

    /**
     * Adds Listener.
     *
     * @param listener the Listener
     */
    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        listeners.add(listener);
    }

    /**
     * Removes Listener.
     *
     * @param listener the Listener
     */
    public void removeListener(Listener listener) {
        if (listeners == null) {
            return;
        }

        listeners.remove(listener);
    }

    /**
     * Notifies Listeners.
     *
     * @param event the context
     */
    public void notifyListeners(Event event) {
        if (listeners == null) {
            return;
        }

        for (Listener listener : listeners) {
            listener.processEvent(event);
        }
    }
}
