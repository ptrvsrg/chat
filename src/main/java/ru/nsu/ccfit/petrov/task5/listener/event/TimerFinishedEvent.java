package ru.nsu.ccfit.petrov.task5.listener.event;

import ru.nsu.ccfit.petrov.task5.listener.event.processor.EventProcessor;

public class TimerFinishedEvent
    implements Event {

    @Override
    public void process(EventProcessor eventProcessor) {
        eventProcessor.processEvent(this);
    }
}
