package ru.nsu.ccfit.petrov.task5.server.event;

/**
 * The type {@code TimerFinishedEvent} is class that describes event when timer is finished.
 *
 * @author ptrvsrg
 */
public class StopwatchFinishedEvent
    extends ServerEvent {

    /**
     * Instantiates a new {@code TimerFinishedEvent}.
     */
    public StopwatchFinishedEvent() {
        super(ServerEventType.STOPWATCH_FINISHED);
    }
}
