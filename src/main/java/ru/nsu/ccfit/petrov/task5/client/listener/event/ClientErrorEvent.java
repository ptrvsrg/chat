package ru.nsu.ccfit.petrov.task5.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClientErrorEvent
    implements Event {

    private final String reason;
}
