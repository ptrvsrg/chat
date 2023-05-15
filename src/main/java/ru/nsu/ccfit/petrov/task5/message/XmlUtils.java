package ru.nsu.ccfit.petrov.task5.message;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.petrov.task5.message.Message.Subtype;
import ru.nsu.ccfit.petrov.task5.message.Message.Type;

public class XmlUtils {

    private XmlUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Message documentToMessage(Document document) {
        Element rootTag = document.getDocumentElement();

        switch (rootTag.getTagName()) {
            case "request":
                return documentToRequestMessage(document);
            case "response":
                return documentToResponseMessage(document);
            case "event":
                return documentToEventMessage(document);
            default:
                return null;
        }
    }

    private static Message documentToRequestMessage(Document document) {
        Element rootTag = document.getDocumentElement();

        switch (rootTag.getAttribute("name")) {
            case "login":
                return documentToLoginRequestMessage(document);
            case "new_message":
                return documentToNewMessageRequestMessage(document);
            case "user_list":
                return documentToUserListRequestMessage(document);
            case "logout":
                return documentToLogoutRequestMessage(document);
            default:
                return null;
        }
    }

    private static UUID getMessageId(Document document) {
        Element rootTag = document.getDocumentElement();
        String messageId = rootTag.getAttribute("id");
        try {
            return UUID.fromString(messageId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Message documentToLoginRequestMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList userTags = document.getElementsByTagName("user");
        if (userTags.getLength() != 1) {
            return null;
        }
        String userName = userTags.item(0).getTextContent();

        return new Message(id, Type.REQUEST, Subtype.LOGIN, null, userName, null, null);
    }

    private static Message documentToNewMessageRequestMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList messageTags = document.getElementsByTagName("message");
        if (messageTags.getLength() != 1) {
            return null;
        }

        String messageContent = messageTags.item(0).getTextContent();

        return new Message(id, Type.REQUEST, Subtype.NEW_MESSAGE, null, null, messageContent, null);
    }

    private static Message documentToUserListRequestMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        return new Message(id, Type.REQUEST, Subtype.USER_LIST, null, null, null, null);
    }

    private static Message documentToLogoutRequestMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        return new Message(id, Type.REQUEST, Subtype.LOGOUT, null, null, null, null);
    }

    private static Message documentToResponseMessage(Document document) {
        Element rootTag = document.getDocumentElement();

        switch (rootTag.getAttribute("name")) {
            case "success":
                return documentToSuccessResponseMessage(document);
            case "error":
                return documentToErrorResponseMessage(document);
            default:
                return null;
        }
    }

    private static Message documentToSuccessResponseMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList requestTags = document.getElementsByTagName("request");
        NodeList usersTags = document.getElementsByTagName("users");

        if (requestTags.getLength() != 1) {
            return null;
        }

        UUID requestId;
        try {
            requestId = UUID.fromString(requestTags.item(0).getTextContent());
        } catch (IllegalArgumentException e) {
            return null;
        }

        String[] users = null;
        if (usersTags.getLength() == 1) {
            NodeList userTags = usersTags.item(0).getChildNodes();

            users = new String[userTags.getLength()];
            for (int i = 0; i < userTags.getLength(); ++i) {
                users[i] = userTags.item(i).getTextContent();
            }
        }

        return new Message(id, Type.RESPONSE, Subtype.SUCCESS, requestId, null, null, users);
    }

    private static Message documentToErrorResponseMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList requestTags = document.getElementsByTagName("request");
        NodeList reasonTags = document.getElementsByTagName("reason");
        if (requestTags.getLength() != 1 || reasonTags.getLength() != 1) {
            return null;
        }

        String reason = reasonTags.item(0).getTextContent();
        UUID requestId;
        try {
            requestId = UUID.fromString(requestTags.item(0).getTextContent());
        } catch (IllegalArgumentException e) {
            return null;
        }

        return new Message(id, Type.RESPONSE, Subtype.ERROR, requestId, null, reason, null);
    }

    private static Message documentToEventMessage(Document document) {
        Element rootTag = document.getDocumentElement();

        switch (rootTag.getAttribute("name")) {
            case "login":
                return documentToLoginEventMessage(document);
            case "new_message":
                return documentToNewMessageEventMessage(document);
            case "logout":
                return documentToLogoutEventMessage(document);
            default:
                return null;
        }
    }

    private static Message documentToLoginEventMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList userTags = document.getElementsByTagName("user");
        if (userTags.getLength() != 1) {
            return null;
        }

        String userName = userTags.item(0).getTextContent();

        return new Message(id, Type.EVENT, Subtype.LOGIN, null, userName, null, null);
    }

    private static Message documentToNewMessageEventMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList userTags = document.getElementsByTagName("user");
        NodeList messageTags = document.getElementsByTagName("message");
        if (userTags.getLength() != 1 || messageTags.getLength() != 1) {
            return null;
        }

        String userName = userTags.item(0).getTextContent();
        String messageContent = messageTags.item(0).getTextContent();

        return new Message(id, Type.EVENT, Subtype.NEW_MESSAGE, null, userName, messageContent, null);
    }

    private static Message documentToLogoutEventMessage(Document document) {
        UUID id = getMessageId(document);
        if (id == null) {
            return null;
        }

        NodeList userTags = document.getElementsByTagName("user");
        if (userTags.getLength() != 1) {
            return null;
        }

        String userName = userTags.item(0).getTextContent();

        return new Message(id, Type.EVENT, Subtype.LOGOUT, null, userName, null, null);
    }

    public static Document messageToDocument(Message message)
        throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        switch (message.getType()) {
            case REQUEST:
                return requestMessageToDocument(message, document);
            case RESPONSE:
                return responseMessageToDocument(message, document);
            case EVENT:
                return eventMessageToDocument(message, document);
            default:
                return null;
        }
    }

    private static Document requestMessageToDocument(Message message, Document document) {
        Element rootTag = document.createElement("request");
        rootTag.setAttribute("id", message.getId().toString());
        document.appendChild(rootTag);

        switch (message.getSubtype()) {
            case LOGIN:
                return loginRequestMessageToDocument(message, document);
            case NEW_MESSAGE:
                return newMessageRequestMessageToDocument(message, document);
            case USER_LIST:
                return userListRequestMessageToDocument(message, document);
            case LOGOUT:
                return logoutRequestMessageToDocument(message, document);
            default:
                return null;
        }
    }

    private static Document loginRequestMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "login");

        Element userTag = document.createElement("user");
        userTag.setTextContent(message.getUserName());
        rootTag.appendChild(userTag);

        return document;
    }

    private static Document newMessageRequestMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "new_message");

        Element messageTag = document.createElement("message");
        messageTag.setTextContent(message.getMessageContent());
        rootTag.appendChild(messageTag);

        return document;
    }

    private static Document userListRequestMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "user_list");

        return document;
    }

    private static Document logoutRequestMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "logout");

        return document;
    }

    private static Document responseMessageToDocument(Message message, Document document) {
        Element rootTag = document.createElement("response");
        rootTag.setAttribute("id", message.getId().toString());
        document.appendChild(rootTag);

        Element requestTag = document.createElement("request");
        requestTag.setTextContent(message.getRequestId().toString());
        rootTag.appendChild(requestTag);

        switch (message.getSubtype()) {
            case SUCCESS:
                return successResponseMessageToDocument(message, document);
            case ERROR:
                return errorResponseMessageToDocument(message, document);
            default:
                return null;
        }
    }

    private static Document successResponseMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "success");

        if (message.getUsers() != null) {
            Element usersTag = document.createElement("users");
            rootTag.appendChild(usersTag);

            for (String userName : message.getUsers()) {
                Element userTag = document.createElement("user");
                userTag.setTextContent(userName);
                usersTag.appendChild(userTag);
            }
        }

        return document;
    }

    private static Document errorResponseMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "error");

        Element reasonTag = document.createElement("reason");
        reasonTag.setTextContent(message.getMessageContent());
        rootTag.appendChild(reasonTag);

        return document;
    }

    private static Document eventMessageToDocument(Message message, Document document) {
        Element rootTag = document.createElement("event");
        rootTag.setAttribute("id", message.getId().toString());
        document.appendChild(rootTag);

        switch (message.getSubtype()) {
            case LOGIN:
                return loginEventMessageToDocument(message, document);
            case NEW_MESSAGE:
                return newMessageEventMessageToDocument(message, document);
            case LOGOUT:
                return logoutEventMessageToDocument(message, document);
            default:
                return null;
        }
    }

    private static Document loginEventMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "login");

        Element userTag = document.createElement("user");
        userTag.setTextContent(message.getUserName());
        rootTag.appendChild(userTag);

        return document;
    }

    private static Document newMessageEventMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "new_message");

        Element userTag = document.createElement("user");
        userTag.setTextContent(message.getUserName());
        rootTag.appendChild(userTag);

        Element messageTag = document.createElement("message");
        messageTag.setTextContent(message.getMessageContent());
        rootTag.appendChild(messageTag);

        return document;
    }

    private static Document logoutEventMessageToDocument(Message message, Document document) {
        Element rootTag = document.getDocumentElement();
        rootTag.setAttribute("name", "logout");

        Element userTag = document.createElement("user");
        userTag.setTextContent(message.getUserName());
        rootTag.appendChild(userTag);

        return document;
    }

    public static String documentToString(Document document)
        throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        return writer.getBuffer().toString();
    }

    public static Document stringToDocument(String str)
        throws ParserConfigurationException, IOException, SAXException {
        InputSource inputSource = new InputSource(new StringReader(str));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(inputSource);
    }
}
