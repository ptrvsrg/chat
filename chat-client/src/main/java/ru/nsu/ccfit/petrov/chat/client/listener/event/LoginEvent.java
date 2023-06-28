package ru.nsu.ccfit.petrov.chat.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The type LoginEvent is event that describes registration new user.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class LoginEvent
    implements Event {

    private final String username;
}
