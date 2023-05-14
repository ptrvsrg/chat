package ru.nsu.ccfit.petrov.task5.listener;

import java.util.EventListener;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;

public interface Listener
    extends EventListener {

    void processEvent(Event event);
}
