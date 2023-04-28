package ru.nsu.ccfit.petrov.task5.listener.event;

import ru.nsu.ccfit.petrov.task5.listener.event.processor.EventProcessor;

public class RequestReceivedEvent
    implements Event {

    public void process(EventProcessor eventProcessor) {
        eventProcessor.processEvent(this);
    }
}
