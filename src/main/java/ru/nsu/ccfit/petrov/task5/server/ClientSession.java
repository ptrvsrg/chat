package ru.nsu.ccfit.petrov.task5.server;

import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;
import ru.nsu.ccfit.petrov.task5.listener.event.RequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.SessionTimeoutEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.TimerFinishedEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.processor.AbstractEventProcessor;
import ru.nsu.ccfit.petrov.task5.listener.event.processor.EventProcessor;

public class ClientSession
    implements Listener {

    private final Stopwatch stopwatch;
    private final AbstractRequestHandler requestHandler;
    private final ListeningSupport listeningSupport = new ListeningSupport();

    public ClientSession(AbstractRequestHandler requestHandler, int timeout) {
        this.requestHandler = requestHandler;
        this.stopwatch = new Stopwatch(timeout);

        requestHandler.addListener(this);
        stopwatch.addListener(this);
    }

    public void start() {
        stopwatch.start();
        requestHandler.start();
    }

    public void stop() {
        stopwatch.stop();
        requestHandler.interrupt();
    }

    /**
     * Handles the context of the {@link ListeningSupport Observable} object message.
     *
     * @param event the context
     */
    @Override
    public void processEvent(Event event) {
        EventProcessor eventProcessor = new AbstractEventProcessor() {
            @Override
            public void processEvent(RequestReceivedEvent event) {
                stopwatch.reset();
                listeningSupport.notifyListeners(event);
            }

            @Override
            public void processEvent(TimerFinishedEvent event) {
                listeningSupport.notifyListeners(new SessionTimeoutEvent(this));
            }
        };

        event.process(eventProcessor);
    }

    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    public void removeListener(Listener listener) {
        listeningSupport.removeListener(listener);
    }
}