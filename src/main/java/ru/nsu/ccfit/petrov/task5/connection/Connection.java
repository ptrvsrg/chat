package ru.nsu.ccfit.petrov.task5.connection;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.MessageFormat;
import ru.nsu.ccfit.petrov.task5.message.XmlUtils;
import ru.nsu.ccfit.petrov.task5.server.config.ServerConfig;

@Log4j2
public class Connection
    implements Closeable {

    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket clientSocket)
        throws IOException {
        this.clientSocket = clientSocket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void send(Message message)
        throws IOException {
        MessageFormat messageFormat = ServerConfig.getMessageFormat();

        switch (messageFormat) {
            case JAVA_OBJECT:
                sendJavaObject(message);
                break;
            case XML_FILE:
                sendXmlFile(message);
                break;
        }
    }

    private void sendJavaObject(Message message)
        throws IOException {
        out.writeObject(message);
    }

    private void sendXmlFile(Message message)
        throws IOException {
        String xmlMessage;
        try {
            Document document = XmlUtils.messageToDocument(message);
            xmlMessage = XmlUtils.documentToString(document);
        } catch (ParserConfigurationException | TransformerException e) {
            return;
        }

        out.writeObject(xmlMessage);
    }

    public Message receive()
        throws IOException {
        MessageFormat messageFormat = ServerConfig.getMessageFormat();

        switch (messageFormat) {
            case JAVA_OBJECT:
                return receiveJavaObject();
            case XML_FILE:
                return receiveXmlFile();
        }

        return null;
    }

    private Message receiveJavaObject()
        throws IOException {
        try {
            Object o = in.readObject();
            return (Message) o;
        } catch (ClassNotFoundException | ClassCastException e) {
            return null;
        }
    }

    private Message receiveXmlFile()
        throws IOException {
        String xmlMessage;
        try {
            xmlMessage = (String) in.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            return null;
        }

        try {
            return XmlUtils.documentToMessage(XmlUtils.stringToDocument(xmlMessage));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
    }

    @Override
    public void close()
        throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }
}