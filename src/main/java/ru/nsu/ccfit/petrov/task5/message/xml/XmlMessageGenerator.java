package ru.nsu.ccfit.petrov.task5.message.xml;

import java.util.Set;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.nsu.ccfit.petrov.task5.message.Message.MessageSubtype;
import ru.nsu.ccfit.petrov.task5.message.Message.MessageType;
import ru.nsu.ccfit.petrov.task5.message.MessageGenerator;

/**
 * The type {@code XmlMessageGenerator} is class that implements {@link MessageGenerator} for XML file messages.
 *
 * @author ptrvsrg
 */
@Log4j2
public class XmlMessageGenerator
    implements MessageGenerator {

    private final DocumentBuilder documentBuilder;

    /**
     * Instantiates a new {@code XmlMessageGenerator}.
     */
    public XmlMessageGenerator() {
        try {
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.catching(Level.FATAL, e);
            throw new RuntimeException(e);
        }
    }

    private XmlMessage createMessage(Document xmlFile, MessageType messageType, MessageSubtype messageSubtype,
                                     Element[] elements) {
        Element rootTag;
        switch (messageType) {
            case REQUEST:
                rootTag = xmlFile.createElement("request");
                break;
            case RESPONSE:
                rootTag = xmlFile.createElement("response");
                break;
            case EVENT:
                rootTag = xmlFile.createElement("event");
                break;
            default:
                return null;
        }

        String attributeValue;
        switch (messageSubtype) {
            case LOGIN:
                attributeValue = "login";
                break;
            case USER_LIST:
                attributeValue = "user_list";
                break;
            case NEW_MESSAGE:
                attributeValue = "new_message";
                break;
            case LOGOUT:
                attributeValue = "logout";
                break;
            case SUCCESS:
                attributeValue = "success";
                break;
            case ERROR:
                attributeValue = "error";
                break;
            default:
                return null;
        }

        rootTag.setAttribute("name", attributeValue);

        for (Element element : elements) {
            rootTag.appendChild(element);
        }

        xmlFile.appendChild(rootTag);

        return new XmlMessage(xmlFile);
    }

    @Override
    public XmlMessage createLoginEventMessage(String chatName, String userName) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element userTag = xmlMessage.createElement("user");
        userTag.setTextContent(userName);

        return createMessage(xmlMessage, MessageType.EVENT, MessageSubtype.LOGIN, new Element[]{
            chatTag,
            userTag
        });
    }

    @Override
    public XmlMessage createNewMessageEventMessage(String chatName, String userName, String message) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element userTag = xmlMessage.createElement("user");
        userTag.setTextContent(userName);

        Element messageTag = xmlMessage.createElement("message");
        userTag.setTextContent(message);

        return createMessage(xmlMessage, MessageType.EVENT, MessageSubtype.NEW_MESSAGE, new Element[]{
            chatTag,
            userTag,
            messageTag
        });
    }

    @Override
    public XmlMessage createLogoutEventMessage(String chatName, String userName) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element userTag = xmlMessage.createElement("user");
        userTag.setTextContent(userName);

        return createMessage(xmlMessage, MessageType.EVENT, MessageSubtype.LOGOUT, new Element[]{
            chatTag,
            userTag
        });
    }

    @Override
    public XmlMessage createSuccessResponseMessage() {
        Document xmlMessage = documentBuilder.newDocument();

        return createMessage(xmlMessage, MessageType.RESPONSE, MessageSubtype.SUCCESS, new Element[]{});
    }

    @Override
    public XmlMessage createSuccessResponseMessage(UUID sessionId) {
        Document xmlMessage = documentBuilder.newDocument();

        Element sessionTag = xmlMessage.createElement("session");
        sessionTag.setTextContent(sessionId.toString());

        return createMessage(xmlMessage, MessageType.RESPONSE, MessageSubtype.SUCCESS, new Element[]{
            sessionTag
        });
    }

    @Override
    public XmlMessage createSuccessResponseMessage(String chatName, Set<String> userNames) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element usersTag = xmlMessage.createElement("users");

        for (String userName : userNames) {
            Element userTag = xmlMessage.createElement("user");
            userTag.setTextContent(userName);
            usersTag.appendChild(userTag);
        }

        return createMessage(xmlMessage, MessageType.RESPONSE, MessageSubtype.SUCCESS, new Element[]{
            chatTag,
            usersTag
        });
    }

    @Override
    public XmlMessage createErrorResponseMessage(String reason) {
        Document xmlMessage = documentBuilder.newDocument();

        Element messageTag = xmlMessage.createElement("message");
        messageTag.setTextContent(reason);

        return createMessage(xmlMessage, MessageType.RESPONSE, MessageSubtype.ERROR, new Element[]{
            messageTag
        });
    }

    @Override
    public XmlMessage createLoginRequestMessage(String chatName, String userName) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element userTag = xmlMessage.createElement("user");
        userTag.setTextContent(userName);

        return createMessage(xmlMessage, MessageType.REQUEST, MessageSubtype.LOGIN, new Element[]{
            chatTag,
            userTag
        });
    }

    @Override
    public XmlMessage createNewMessageRequestMessage(String chatName, UUID sessionId, String message) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element sessionTag = xmlMessage.createElement("session");
        sessionTag.setTextContent(sessionId.toString());

        Element messageTag = xmlMessage.createElement("message");
        messageTag.setTextContent(message);

        return createMessage(xmlMessage, MessageType.REQUEST, MessageSubtype.NEW_MESSAGE, new Element[]{
            chatTag,
            sessionTag,
            messageTag
        });
    }

    @Override
    public XmlMessage createUserListRequestMessage(String chatName, UUID sessionId) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element sessionTag = xmlMessage.createElement("session");
        sessionTag.setTextContent(sessionId.toString());

        return createMessage(xmlMessage, MessageType.REQUEST, MessageSubtype.USER_LIST, new Element[]{
            chatTag,
            sessionTag
        });
    }

    @Override
    public XmlMessage createLogoutRequestMessage(String chatName, UUID sessionId) {
        Document xmlMessage = documentBuilder.newDocument();

        Element chatTag = xmlMessage.createElement("chat");
        chatTag.setTextContent(chatName);

        Element sessionTag = xmlMessage.createElement("session");
        sessionTag.setTextContent(sessionId.toString());

        return createMessage(xmlMessage, MessageType.REQUEST, MessageSubtype.LOGOUT, new Element[]{
            chatTag,
            sessionTag
        });
    }
}
