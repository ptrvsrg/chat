package ru.nsu.ccfit.petrov.task5.message;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Message
    implements Serializable {

    public enum Type {
        REQUEST,
        RESPONSE,
        EVENT
    }

    public enum Subtype {
        LOGIN,
        NEW_MESSAGE,
        USER_LIST,
        LOGOUT,
        SUCCESS,
        ERROR
    }

    private final UUID id = UUID.randomUUID();
    private final Type type;
    private final Subtype subtype;
    private final UUID requestId;
    private final String userName;
    private final String messageContent;
    private final String[] users;

    public static Message newLoginRequest(String userName) {
        return new Message(Type.REQUEST, Subtype.LOGIN, null, userName, null, null);
    }

    public static Message newNewMessageRequest(String messageContent) {
        return new Message(Type.REQUEST, Subtype.NEW_MESSAGE, null, null, messageContent, null);
    }

    public static Message newUserListRequest() {
        return new Message(Type.REQUEST, Subtype.USER_LIST, null, null, null, null);
    }

    public static Message newLogoutRequest() {
        return new Message(Type.REQUEST, Subtype.LOGOUT, null, null, null, null);
    }

    public static Message newSuccessResponse(UUID requestId) {
        return new Message(Type.RESPONSE, Subtype.SUCCESS, requestId, null, null, null);
    }

    public static Message newSuccessResponse(UUID requestId, Set<String> users) {
        return new Message(Type.RESPONSE, Subtype.SUCCESS, requestId, null, null,
                           users.toArray(String[]::new));
    }

    public static Message newErrorResponse(UUID requestId, String reason) {
        return new Message(Type.RESPONSE, Subtype.ERROR, requestId, null, reason, null);
    }

    public static Message newLoginEvent(String userName) {
        return new Message(Type.EVENT, Subtype.LOGIN, null, userName, null, null);
    }

    public static Message newNewMessageEvent(String userName, String messageContent) {
        return new Message(Type.EVENT, Subtype.NEW_MESSAGE, null, userName, messageContent, null);
    }

    public static Message newLogoutEvent(String userName) {
        return new Message(Type.EVENT, Subtype.LOGOUT, null, userName, null, null);
    }
}
