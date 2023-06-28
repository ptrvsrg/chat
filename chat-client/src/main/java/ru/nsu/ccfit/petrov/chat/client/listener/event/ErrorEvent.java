package ru.nsu.ccfit.petrov.chat.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The type ErrorEvent is event that describes error and its reason.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class ErrorEvent
    implements Event {

    private final String reason;
}
