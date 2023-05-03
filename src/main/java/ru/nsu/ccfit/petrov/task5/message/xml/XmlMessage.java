package ru.nsu.ccfit.petrov.task5.message.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.nsu.ccfit.petrov.task5.message.Message;

/**
 * The type {@code XmlMessage} is class that extends {@link Message} for sending XML files over sockets.
 *
 * @author ptrvsrg
 */
public class XmlMessage
    extends Message {

    /**
     * Instantiates a new {@code XmlMessage}.
     *
     * @param xmlFile the xml file
     */
    public XmlMessage(Document xmlFile) {
        super(null, null, null);

        initMessageType(xmlFile);
        initMessageSubtype(xmlFile);
        initData(xmlFile);
    }

    private void initMessageType(Document xmlFile) {
        Element rootTag = xmlFile.getDocumentElement();
        switch (rootTag.getTagName()) {
            case "request":
                messageType = MessageType.REQUEST;
                break;
            case "response":
                messageType = MessageType.RESPONSE;
                break;
            case "event":
                messageType = MessageType.EVENT;
                break;
            default:
                messageType = null;
        }
    }

    private void initMessageSubtype(Document xmlFile) {
        Element rootTag = xmlFile.getDocumentElement();
        switch (rootTag.getAttribute("name")) {
            case "login":
                messageSubtype = MessageSubtype.LOGIN;
                break;
            case "new_message":
                messageSubtype = MessageSubtype.NEW_MESSAGE;
                break;
            case "user_list":
                messageSubtype = MessageSubtype.USER_LIST;
                break;
            case "logout":
                messageSubtype = MessageSubtype.LOGOUT;
                break;
            case "success":
                messageSubtype = MessageSubtype.SUCCESS;
                break;
            case "error":
                messageSubtype = MessageSubtype.ERROR;
                break;
            default:
                messageSubtype = null;
        }
    }

    private void initData(Document xmlFile) {
        Element rootTag = xmlFile.getDocumentElement();
        NodeList childElements = rootTag.getChildNodes();
        data = new Object[childElements.getLength()];

        for (int i = 0; i < childElements.getLength(); ++i) {
            data[i] = childElements.item(i);
        }
    }
}
