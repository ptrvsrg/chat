package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.IOException;
import java.net.Socket;
import javax.xml.bind.JAXBException;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.XmlUtils;

public class XmlFileConnection
    extends Connection {

    public XmlFileConnection(Socket clientSocket)
        throws IOException {
        super(clientSocket);
    }

    @Override
    public void send(DTO dto)
        throws IOException {
        String xml;
        try {
            xml = XmlUtils.dtoToXml(dto);
        } catch (JAXBException e) {
            throw new IOException("Invalid DTO format : " + e);
        }

        synchronized (out) {
            out.writeObject(xml);
        }
    }

    @Override
    public DTO receive()
        throws IOException {
        String xml;
        try {
            synchronized (in) {
                xml = (String) in.readObject();
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Invalid message format : " + e);
        }

        try {
            return XmlUtils.xmlToDto(xml);
        } catch (JAXBException e) {
            throw new IOException("Invalid message format : " + e);
        }
    }
}