package ru.nsu.ccfit.petrov.chat.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The type NewMessageEvent is event that describes sending new user message.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class NewMessageEvent
    implements Event {

    private final String username;
    private final boolean isCurrentUser;
    private final String message;
}
