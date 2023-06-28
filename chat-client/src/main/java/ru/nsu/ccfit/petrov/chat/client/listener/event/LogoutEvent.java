package ru.nsu.ccfit.petrov.chat.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The type LogoutEvent is event that describes disconnecting user.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class LogoutEvent
    implements Event {

    private final String username;
}
