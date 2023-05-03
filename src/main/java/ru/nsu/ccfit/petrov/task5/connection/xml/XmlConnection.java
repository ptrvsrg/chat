package ru.nsu.ccfit.petrov.task5.connection.xml;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.petrov.task5.connection.Connection;
import ru.nsu.ccfit.petrov.task5.message.Message;
import ru.nsu.ccfit.petrov.task5.message.xml.XmlMessage;

/**
 * The type {@code XmlConnection} is class that implements abstract method of {@link Connection} for sending and
 * receiving XML files.
 *
 * @author ptrvsrg
 */
@Log4j2
public class XmlConnection
    extends Connection {

    /**
     * Instantiates a new {@code XmlConnection}.
     *
     * @param socket the socket
     * @throws IOException if an I/O error occurs when creating the input stream, the socket is closed, the socket is
     *                     not connected, or the socket input has been shutdown using
     *                     {@link java.net.Socket#shutdownInput()}
     */
    public XmlConnection(Socket socket)
        throws IOException {
        super(socket);
    }

    @Override
    public boolean send(Message message) {
        if (!Objects.equals(message.getClass(), XmlMessage.class)) {
            log.error("Invalid message format");
            return false;
        }

        try {
            out.writeObject(message.toString());
        } catch (IOException e) {
            log.catching(Level.ERROR, e);
            return false;
        }

        return true;
    }

    @Override
    public Message receive() {
        String message;
        try {
            message = (String) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            log.catching(Level.ERROR, e);
            return null;
        } catch (ClassCastException e) {
            log.error("Invalid message format");
            return null;
        }

        Document xmlFile;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlFile = builder.parse(message);
        } catch (IOException  e) {
            log.catching(Level.ERROR, e);
            return null;
        } catch (ParserConfigurationException | SAXException e) {
            log.error("Invalid message format");
            return null;
        }

        return new XmlMessage(xmlFile);
    }
}
