package ru.nsu.ccfit.petrov.task5.listener.event;

import ru.nsu.ccfit.petrov.task5.listener.event.processor.EventProcessor;

public interface Event {

    void process(EventProcessor eventProcessor);
}
