package ru.nsu.ccfit.petrov.task5.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@XmlRootElement
public class DTO
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

    @XmlAttribute
    private final UUID id;
    @XmlAttribute
    private final Type type;
    @XmlAttribute
    private final Subtype subtype;
    @XmlElement
    private final UUID requestId;
    @XmlElement
    private final String username;
    @XmlElement
    private final String message;
    @XmlElement
    private final String[] users;

    public DTO(Type type, Subtype subtype, UUID requestId, String username,
               String message, String[] users) {
        this(UUID.randomUUID(), type, subtype, requestId, username, message, users);
    }


    public static DTO newLoginRequest(String userName) {
        return new DTO(Type.REQUEST, Subtype.LOGIN, null, userName, null, null);
    }

    public static DTO newNewMessageRequest(String messageContent) {
        return new DTO(Type.REQUEST, Subtype.NEW_MESSAGE, null, null, messageContent, null);
    }

    public static DTO newUserListRequest() {
        return new DTO(Type.REQUEST, Subtype.USER_LIST, null, null, null, null);
    }

    public static DTO newLogoutRequest() {
        return new DTO(Type.REQUEST, Subtype.LOGOUT, null, null, null, null);
    }

    public static DTO newSuccessResponse(UUID requestId) {
        return new DTO(Type.RESPONSE, Subtype.SUCCESS, requestId, null, null, null);
    }

    public static DTO newSuccessResponse(UUID requestId, String[] users) {
        return new DTO(Type.RESPONSE, Subtype.SUCCESS, requestId, null, null, users);
    }

    public static DTO newErrorResponse(UUID requestId, String reason) {
        return new DTO(Type.RESPONSE, Subtype.ERROR, requestId, null, reason, null);
    }

    public static DTO newLoginEvent(String userName) {
        return new DTO(Type.EVENT, Subtype.LOGIN, null, userName, null, null);
    }

    public static DTO newNewMessageEvent(String userName, String messageContent) {
        return new DTO(Type.EVENT, Subtype.NEW_MESSAGE, null, userName, messageContent, null);
    }

    public static DTO newLogoutEvent(String userName) {
        return new DTO(Type.EVENT, Subtype.LOGOUT, null, userName, null, null);
    }
}
