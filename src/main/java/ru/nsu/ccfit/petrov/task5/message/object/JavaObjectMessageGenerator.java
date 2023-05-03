package ru.nsu.ccfit.petrov.task5.message.object;

import java.util.List;
import java.util.UUID;
import ru.nsu.ccfit.petrov.task5.message.Message.MessageSubtype;
import ru.nsu.ccfit.petrov.task5.message.Message.MessageType;
import ru.nsu.ccfit.petrov.task5.message.MessageGenerator;

/**
 * The type {@code JavaObjectMessageGenerator} is class that implements {@link MessageGenerator} for Java object
 * messages.
 *
 * @author ptrvsrg
 */
public class JavaObjectMessageGenerator
    implements MessageGenerator {

    @Override
    public JavaObjectMessage createLoginEventMessage(String chatName, String userName) {
        return new JavaObjectMessage(MessageType.EVENT, MessageSubtype.LOGIN, new Object[]{
            chatName,
            userName
        });
    }

    @Override
    public JavaObjectMessage createNewMessageEventMessage(String chatName, String userName, String message) {
        return new JavaObjectMessage(MessageType.EVENT, MessageSubtype.NEW_MESSAGE, new Object[]{
            chatName,
            userName,
            message
        });
    }

    @Override
    public JavaObjectMessage createLogoutEventMessage(String chatName, String userName) {
        return new JavaObjectMessage(MessageType.EVENT, MessageSubtype.LOGOUT, new Object[]{
            chatName,
            userName
        });
    }

    @Override
    public JavaObjectMessage createSuccessResponseMessage() {
        return new JavaObjectMessage(MessageType.RESPONSE, MessageSubtype.SUCCESS, new Object[]{});
    }

    @Override
    public JavaObjectMessage createSuccessResponseMessage(UUID sessionId) {
        return new JavaObjectMessage(MessageType.RESPONSE, MessageSubtype.SUCCESS, new Object[]{
            sessionId
        });
    }

    @Override
    public JavaObjectMessage createSuccessResponseMessage(String chatName, List<String> userNames) {
        return new JavaObjectMessage(MessageType.RESPONSE, MessageSubtype.SUCCESS, new Object[]{
            chatName,
            userNames
        });
    }

    @Override
    public JavaObjectMessage createErrorResponseMessage(String reason) {
        return new JavaObjectMessage(MessageType.RESPONSE, MessageSubtype.ERROR, new Object[]{
            reason
        });
    }

    @Override
    public JavaObjectMessage createLoginRequestMessage(String chatName, String userName) {
        return new JavaObjectMessage(MessageType.REQUEST, MessageSubtype.LOGIN, new Object[]{
            chatName,
            userName
        });
    }

    @Override
    public JavaObjectMessage createNewMessageRequestMessage(String chatName, UUID sessionId, String message) {
        return new JavaObjectMessage(MessageType.REQUEST, MessageSubtype.NEW_MESSAGE, new Object[]{
            chatName,
            sessionId,
            message
        });
    }

    @Override
    public JavaObjectMessage createUserListRequestMessage(String chatName, UUID sessionId) {
        return new JavaObjectMessage(MessageType.REQUEST, MessageSubtype.USER_LIST, new Object[]{
            chatName,
            sessionId
        });
    }

    @Override
    public JavaObjectMessage createLogoutRequestMessage(String chatName, UUID sessionId) {
        return new JavaObjectMessage(MessageType.REQUEST, MessageSubtype.LOGOUT, new Object[]{
            chatName,
            sessionId
        });
    }
}
