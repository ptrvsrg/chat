package ru.nsu.ccfit.petrov.task5.server.connection.xml;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.w3c.dom.Element;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.server.connection.ServerConnectionEventProcessor;
import ru.nsu.ccfit.petrov.task5.server.event.LoginRequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.server.event.LogoutRequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.server.event.NewMessageRequestReceivedEvent;
import ru.nsu.ccfit.petrov.task5.server.event.UserListRequestReceivedEvent;

/**
 * The type {@code XmlServerConnectionEventProcessor} is class that implements abstract method of
 * {@link ServerConnectionEventProcessor} for processing XML file messages.
 */
public class XmlServerConnectionEventProcessor
    extends ServerConnectionEventProcessor {

    /**
     * Instantiates a new {@code XmlServerConnectionEventProcessor}.
     *
     * @param connections the connections
     */
    public XmlServerConnectionEventProcessor(Map<UUID, Connection> connections) {
        super(connections);
    }

    @Override
    protected void processLoginRequest(UUID connectionId, Object[] data) {
        Element chatTag;
        Element userTag;
        try {
            chatTag = (Element) data[0];
            userTag = (Element) data[1];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            return;
        }

        if (!Objects.equals(chatTag.getTagName(), "chat")) {
            return;
        }

        if (!Objects.equals(userTag.getTagName(), "user")) {
            return;
        }

        String chatName = chatTag.getTextContent();
        String userName = userTag.getTextContent();

        listeningSupport.notifyListeners(new LoginRequestReceivedEvent(connectionId, chatName, userName));
    }

    @Override
    protected void processUserListRequest(UUID connectionId, Object[] data) {
        Element chatTag;
        Element sessionTag;
        try {
            chatTag = (Element) data[0];
            sessionTag = (Element) data[1];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            return;
        }

        if (!Objects.equals(chatTag.getTagName(), "chat")) {
            return;
        }

        if (!Objects.equals(sessionTag.getTagName(), "session")) {
            return;
        }

        String chatName = chatTag.getTextContent();
        UUID sessionId;
        try {
            sessionId = UUID.fromString(sessionTag.getTextContent());
        } catch (IllegalArgumentException e) {
            return;
        }

        listeningSupport.notifyListeners(new UserListRequestReceivedEvent(connectionId, chatName, sessionId));
    }

    @Override
    protected void processNewMessageRequest(UUID connectionId, Object[] data) {
        Element chatTag;
        Element sessionTag;
        Element messageTag;
        try {
            chatTag = (Element) data[0];
            sessionTag = (Element) data[1];
            messageTag = (Element) data[2];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            return;
        }

        if (!Objects.equals(chatTag.getTagName(), "chat")) {
            return;
        }
        if (!Objects.equals(sessionTag.getTagName(), "session")) {
            return;
        }
        if (!Objects.equals(messageTag.getTagName(), "message")) {
            return;
        }

        String chatName = chatTag.getTextContent();
        UUID sessionId;
        try {
            sessionId = UUID.fromString(sessionTag.getTextContent());
        } catch (IllegalArgumentException e) {
            return;
        }
        String message = messageTag.getTextContent();

        listeningSupport.notifyListeners(new NewMessageRequestReceivedEvent(connectionId, chatName, sessionId, message));
    }

    @Override
    protected void processLogoutRequest(UUID connectionId, Object[] data) {
        Element chatTag;
        Element sessionTag;
        try {
            chatTag = (Element) data[0];
            sessionTag = (Element) data[1];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            return;
        }

        if (!Objects.equals(chatTag.getTagName(), "chat")) {
            return;
        }
        if (!Objects.equals(sessionTag.getTagName(), "session")) {
            return;
        }

        String chatName = chatTag.getTextContent();
        UUID sessionId;
        try {
            sessionId = UUID.fromString(sessionTag.getTextContent());
        } catch (IllegalArgumentException e) {
            return;
        }

        listeningSupport.notifyListeners(new LogoutRequestReceivedEvent(connectionId, chatName, sessionId));
    }
}
