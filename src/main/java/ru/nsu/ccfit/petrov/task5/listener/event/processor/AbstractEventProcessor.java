package ru.nsu.ccfit.petrov.task5.listener.event.processor;

import ru.nsu.ccfit.petrov.task5.listener.event.RequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.SessionTimeoutEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.TimerFinishedEvent;

public class AbstractEventProcessor
    implements EventProcessor {

    @Override
    public void processEvent(RequestReceivedEvent event) {}

    @Override
    public void processEvent(SessionTimeoutEvent event) {}

    @Override
    public void processEvent(TimerFinishedEvent event) {}
}
