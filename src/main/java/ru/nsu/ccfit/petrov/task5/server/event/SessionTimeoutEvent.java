package ru.nsu.ccfit.petrov.task5.server.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.server.session.Session;

/**
 * The type {@code SessionTimeoutEvent} is class that describes event when session timeout is happened.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class SessionTimeoutEvent
    implements ServerEvent {

    private final Session session;
}
