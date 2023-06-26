package ru.nsu.ccfit.petrov.chat.core.dto;

import java.io.Serializable;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
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

    @XmlAttribute UUID id;
    @XmlAttribute Type type;
    @XmlAttribute Subtype subtype;
    @XmlElement UUID requestId;
    @XmlElement String username;
    @XmlElement String message;
    @XmlElementWrapper(name = "users") @XmlElement(name = "user") String[] users;

    public DTO() {
        this(null, null, null, null, null, null);
    }

    public DTO(Type type, Subtype subtype, UUID requestId, String username, String message,
               String[] users) {
        this(UUID.randomUUID(), type, subtype, requestId, username, message, users);
    }

    public static DTO newLoginRequest(String username) {
        return new DTO(Type.REQUEST, Subtype.LOGIN, null, username, null, null);
    }

    public static DTO newNewMessageRequest(String message) {
        return new DTO(Type.REQUEST, Subtype.NEW_MESSAGE, null, null, message, null);
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

    public static DTO newLoginEvent(String username) {
        return new DTO(Type.EVENT, Subtype.LOGIN, null, username, null, null);
    }

    public static DTO newNewMessageEvent(String username, String message) {
        return new DTO(Type.EVENT, Subtype.NEW_MESSAGE, null, username, message, null);
    }

    public static DTO newLogoutEvent(String username) {
        return new DTO(Type.EVENT, Subtype.LOGOUT, null, username, null, null);
    }

    public static boolean isRequest(DTO dto) {
        return dto.getType() == Type.REQUEST;
    }

    public static boolean isResponse(DTO dto) {
        return dto.getType() == Type.RESPONSE;
    }

    public static boolean isEvent(DTO dto) {
        return dto.getType() == Type.EVENT;
    }

    public static boolean isLoginRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.LOGIN;
    }

    public static boolean isNewMessageRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.NEW_MESSAGE;
    }

    public static boolean isUserListRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.USER_LIST;
    }

    public static boolean isLogoutRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.LOGOUT;
    }

    public static boolean isSuccessResponse(DTO dto) {
        return dto.getType() == Type.RESPONSE && dto.getSubtype() == Subtype.SUCCESS;
    }

    public static boolean isErrorResponse(DTO dto) {
        return dto.getType() == Type.RESPONSE && dto.getSubtype() == Subtype.ERROR;
    }

    public static boolean isLoginEvent(DTO dto) {
        return dto.getType() == Type.EVENT && dto.getSubtype() == Subtype.LOGIN;
    }

    public static boolean isNewMessageEvent(DTO dto) {
        return dto.getType() == Type.EVENT && dto.getSubtype() == Subtype.NEW_MESSAGE;
    }

    public static boolean isLogoutEvent(DTO dto) {
        return dto.getType() == Type.EVENT && dto.getSubtype() == Subtype.LOGOUT;
    }
}
