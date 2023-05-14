package ru.nsu.ccfit.petrov.task5.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NewMessageEvent
    implements Event {

    private final String userName;
    private final String messageContent;
}
