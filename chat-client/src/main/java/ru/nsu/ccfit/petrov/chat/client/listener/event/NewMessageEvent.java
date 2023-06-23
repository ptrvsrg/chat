package ru.nsu.ccfit.petrov.chat.client.listener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NewMessageEvent
    implements Event {

    private final String username;
    private final boolean isCurrentUser;
    private final String message;
}
