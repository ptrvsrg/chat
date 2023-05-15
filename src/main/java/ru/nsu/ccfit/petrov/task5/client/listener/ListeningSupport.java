package ru.nsu.ccfit.petrov.task5.client.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.client.listener.event.Event;

@Getter
public class ListeningSupport {

    private final Set<Listener> listeners = new HashSet<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void addListeners(Collection<? extends Listener> listeners) {
        this.listeners.addAll(listeners);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(Event event) {
        for (Listener listener : listeners) {
            listener.processEvent(event);
        }
    }
}
