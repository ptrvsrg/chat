package ru.nsu.ccfit.petrov.task5.listener.event;

import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.listener.event.processor.EventProcessor;

public class SessionTimeoutEvent
    implements Event {

    @Getter private final Object source;

    public SessionTimeoutEvent(Object source) {
        this.source = source;
    }

    @Override
    public void process(EventProcessor eventProcessor) {
        eventProcessor.processEvent(this);
    }
}
