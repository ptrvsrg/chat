package ru.nsu.ccfit.petrov.task5.server.event;

/**
 * The type {@code TimerFinishedEvent} is class that describes event when timer is finished.
 *
 * @author ptrvsrg
 */
public class TimerFinishedEvent
    extends ServerEvent {

    /**
     * Instantiates a new {@code TimerFinishedEvent}.
     */
    public TimerFinishedEvent() {
        super(ServerEventType.TIMER_FINISHED);
    }
}
