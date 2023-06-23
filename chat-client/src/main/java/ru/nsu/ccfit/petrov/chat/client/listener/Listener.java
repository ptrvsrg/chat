package ru.nsu.ccfit.petrov.chat.client.listener;

import java.util.EventListener;
import ru.nsu.ccfit.petrov.chat.client.listener.event.Event;

public interface Listener
    extends EventListener {

    void processEvent(Event event);
}
