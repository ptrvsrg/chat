package ru.nsu.ccfit.petrov.chat.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorEvent
    implements Event {

    private final String reason;
}
