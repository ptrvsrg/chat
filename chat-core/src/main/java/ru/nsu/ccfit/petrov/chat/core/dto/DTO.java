package ru.nsu.ccfit.petrov.chat.core.dto;

import java.io.Serializable;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * The type DTO is class that describes data transfer objects and contains utility methods for instantiation and recognizing DTO type.
 *
 * @author ptrvsrg
 */
@Value
@RequiredArgsConstructor
@XmlRootElement
public class DTO
    implements Serializable {

    /**
     * The enum Type is enum that contains available DTO type.
     */
    public enum Type {
        /**
         * Request type.
         */
        REQUEST,
        /**
         * Response type.
         */
        RESPONSE,
        /**
         * Event type.
         */
        EVENT
    }

    /**
     * The enum Subtype is enum that contains all available subtype of types
     */
    public enum Subtype {
        /**
         * Login subtype.
         */
        LOGIN,
        /**
         * New message subtype.
         */
        NEW_MESSAGE,
        /**
         * User list subtype.
         */
        USER_LIST,
        /**
         * Logout subtype.
         */
        LOGOUT,
        /**
         * Success subtype.
         */
        SUCCESS,
        /**
         * Error subtype.
         */
        ERROR
    }

    @XmlAttribute UUID id;
    @XmlAttribute Type type;
    @XmlAttribute Subtype subtype;
    @XmlElement UUID requestId;
    @XmlElement String username;
    @XmlElement String message;
    @XmlElementWrapper(name = "users") @XmlElement(name = "user") String[] users;

    /**
     * Instantiate a null DTO.
     */
    public DTO() {
        this(null, null, null, null, null, null);
    }

    /**
     * Instantiate a new DTO.
     *
     * @param type      the type
     * @param subtype   the subtype
     * @param requestId the request ID
     * @param username  the username
     * @param message   the message
     * @param users     the users
     */
    public DTO(Type type, Subtype subtype, UUID requestId, String username, String message,
               String[] users) {
        this(UUID.randomUUID(), type, subtype, requestId, username, message, users);
    }

    /**
     * Instantiate a new DTO that describes login request.
     *
     * @param username the username
     * @return the DTO
     */
    public static DTO newLoginRequest(String username) {
        return new DTO(Type.REQUEST, Subtype.LOGIN, null, username, null, null);
    }

    /**
     * Instantiate a new DTO that describes new message request.
     *
     * @param message the message
     * @return the DTO
     */
    public static DTO newNewMessageRequest(String message) {
        return new DTO(Type.REQUEST, Subtype.NEW_MESSAGE, null, null, message, null);
    }

    /**
     * Instantiate a new DTO that describes user list request.
     *
     * @return the DTO
     */
    public static DTO newUserListRequest() {
        return new DTO(Type.REQUEST, Subtype.USER_LIST, null, null, null, null);
    }

    /**
     * Instantiate a new DTO that describes logout request.
     *
     * @return the DTO
     */
    public static DTO newLogoutRequest() {
        return new DTO(Type.REQUEST, Subtype.LOGOUT, null, null, null, null);
    }

    /**
     * Instantiate a new DTO that describes success response.
     *
     * @param requestId the request ID
     * @return the DTO
     */
    public static DTO newSuccessResponse(UUID requestId) {
        return new DTO(Type.RESPONSE, Subtype.SUCCESS, requestId, null, null, null);
    }

    /**
     * Instantiate a new DTO that describes success response.
     *
     * @param requestId the request ID
     * @param users     the users
     * @return the DTO
     */
    public static DTO newSuccessResponse(UUID requestId, String[] users) {
        return new DTO(Type.RESPONSE, Subtype.SUCCESS, requestId, null, null, users);
    }

    /**
     * Instantiate a new DTO that describes error response.
     *
     * @param requestId the request ID
     * @param reason    the reason
     * @return the DTO
     */
    public static DTO newErrorResponse(UUID requestId, String reason) {
        return new DTO(Type.RESPONSE, Subtype.ERROR, requestId, null, reason, null);
    }

    /**
     * Instantiate a new DTO that describes login event.
     *
     * @param username the username
     * @return the DTO
     */
    public static DTO newLoginEvent(String username) {
        return new DTO(Type.EVENT, Subtype.LOGIN, null, username, null, null);
    }

    /**
     * Instantiate a new DTO that describes new message event.
     *
     * @param username the username
     * @param message  the message
     * @return the DTO
     */
    public static DTO newNewMessageEvent(String username, String message) {
        return new DTO(Type.EVENT, Subtype.NEW_MESSAGE, null, username, message, null);
    }

    /**
     * Instantiate a new DTO that describes logout event.
     *
     * @param username the username
     * @return the DTO
     */
    public static DTO newLogoutEvent(String username) {
        return new DTO(Type.EVENT, Subtype.LOGOUT, null, username, null, null);
    }

    /**
     * Is DTO request?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isRequest(DTO dto) {
        return dto.getType() == Type.REQUEST;
    }

    /**
     * Is DTO response?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isResponse(DTO dto) {
        return dto.getType() == Type.RESPONSE;
    }

    /**
     * Is DTO event?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isEvent(DTO dto) {
        return dto.getType() == Type.EVENT;
    }

    /**
     * Is DTO login request?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isLoginRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.LOGIN;
    }

    /**
     * Is DTO new message request?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isNewMessageRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.NEW_MESSAGE;
    }

    /**
     * Is DTO user list request?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isUserListRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.USER_LIST;
    }

    /**
     * Is DTO logout request?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isLogoutRequest(DTO dto) {
        return dto.getType() == Type.REQUEST && dto.getSubtype() == Subtype.LOGOUT;
    }

    /**
     * Is DTO success response?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isSuccessResponse(DTO dto) {
        return dto.getType() == Type.RESPONSE && dto.getSubtype() == Subtype.SUCCESS;
    }

    /**
     * Is DTO error response?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isErrorResponse(DTO dto) {
        return dto.getType() == Type.RESPONSE && dto.getSubtype() == Subtype.ERROR;
    }

    /**
     * Is DTO login event?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isLoginEvent(DTO dto) {
        return dto.getType() == Type.EVENT && dto.getSubtype() == Subtype.LOGIN;
    }

    /**
     * Is DTO new message event?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isNewMessageEvent(DTO dto) {
        return dto.getType() == Type.EVENT && dto.getSubtype() == Subtype.NEW_MESSAGE;
    }

    /**
     * Is DTO logout event?
     *
     * @param dto the DTO
     * @return the boolean
     */
    public static boolean isLogoutEvent(DTO dto) {
        return dto.getType() == Type.EVENT && dto.getSubtype() == Subtype.LOGOUT;
    }
}
