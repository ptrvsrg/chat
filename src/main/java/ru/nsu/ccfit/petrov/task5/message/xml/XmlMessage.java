package ru.nsu.ccfit.petrov.task5.message.xml;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code XmlMessage} is class that implements {@link Message} for sending XML files over sockets.
 *
 * @author ptrvsrg
 */
@Getter
@RequiredArgsConstructor
public class XmlMessage
    implements Message {

    private final Document xmlFile;

    @Override
    public MessageType getMessageType() {
        Element rootTag = xmlFile.getDocumentElement();
        switch (rootTag.getTagName()) {
            case "request":
                return MessageType.REQUEST;
            case "response":
                return MessageType.RESPONSE;
            case "event":
                return MessageType.EVENT;
            default:
                return null;
        }
    }

    @Override
    public MessageSubtype getMessageSubtype() {
        Element rootTag = xmlFile.getDocumentElement();
        switch (rootTag.getAttribute("name")) {
            case "login":
                return MessageSubtype.LOGIN;
            case "new_message":
                return MessageSubtype.NEW_MESSAGE;
            case "user_list":
                return MessageSubtype.USER_LIST;
            case "logout":
                return MessageSubtype.LOGOUT;
            case "success":
                return MessageSubtype.SUCCESS;
            case "error":
                return MessageSubtype.ERROR;
            default:
                return null;
        }
    }

    @Override
    public Object[] getData() {
        Element rootTag = xmlFile.getDocumentElement();
        NodeList childElements = rootTag.getChildNodes();
        Object[] data = new Object[childElements.getLength()];

        for (int i = 0; i < childElements.getLength(); ++i) {
            data[i] = childElements.item(i);
        }

        return data;
    }
}
