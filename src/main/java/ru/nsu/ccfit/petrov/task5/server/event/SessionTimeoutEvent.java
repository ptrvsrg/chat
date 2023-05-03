package ru.nsu.ccfit.petrov.task5.server.event;

import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.server.session.Session;

/**
 * The type {@code SessionTimeoutEvent} is class that describes event when session timeout is happened.
 *
 * @author ptrvsrg
 */
@Getter
public class SessionTimeoutEvent
    extends ServerEvent {

    private final Session session;

    /**
     * Instantiates a new {@code SessionTimeoutEvent}.
     *
     * @param session the session
     */
    public SessionTimeoutEvent(Session session) {
        super(ServerEventType.SESSION_TIMEOUT);
        this.session = session;
    }
}
