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
            default:
                throw new IOException("Invalid message format");
        }
    }

    private void sendJavaObject(Message message)
        throws IOException {
        synchronized (out) {
            out.writeObject(message);
        }
    }

    private void sendXmlFile(Message message)
        throws IOException {
        String xmlMessage;
        try {
            Document document = XmlUtils.messageToDocument(message);
            xmlMessage = XmlUtils.documentToString(document);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new IOException("Invalid message format : " + e);
        }

        synchronized (out) {
            out.writeObject(xmlMessage);
        }
    }

    public Message receive()
        throws IOException {
        MessageFormat messageFormat = ServerConfig.getMessageFormat();

        switch (messageFormat) {
            case JAVA_OBJECT:
                return receiveJavaObject();
            case XML_FILE:
                return receiveXmlFile();
            default:
                throw new IOException("Invalid message format");
        }
    }

    private Message receiveJavaObject()
        throws IOException {
        try {
            synchronized (in) {
                return (Message) in.readObject();
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Invalid message format : " + e);
        }
    }

    private Message receiveXmlFile()
        throws IOException {
        String xmlMessage;
        try {
            synchronized (in) {
                xmlMessage = (String) in.readObject();
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Invalid message format : " + e);
        }

        try {
            return XmlUtils.documentToMessage(XmlUtils.stringToDocument(xmlMessage));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IOException("Invalid message format : " + e);
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