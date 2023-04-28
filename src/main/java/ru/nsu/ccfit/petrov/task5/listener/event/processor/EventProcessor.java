package ru.nsu.ccfit.petrov.task5.listener.event.processor;

import ru.nsu.ccfit.petrov.task5.listener.event.RequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.SessionTimeoutEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.TimerFinishedEvent;

public interface EventProcessor {

    void processEvent(RequestReceivedEvent event);
    void processEvent(SessionTimeoutEvent event);
    void processEvent(TimerFinishedEvent event);
}
