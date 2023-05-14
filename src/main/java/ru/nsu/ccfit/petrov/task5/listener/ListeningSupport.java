package ru.nsu.ccfit.petrov.task5.listener;

import java.util.ArrayList;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;

public class ListeningSupport {

    private ArrayList<Listener> listeners = null;

    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        if (listeners == null) {
            return;
        }

        listeners.remove(listener);
    }

    public void notifyListeners(Event event) {
        if (listeners == null) {
            return;
        }

        for (Listener listener : listeners) {
            listener.processEvent(event);
        }
    }
}
