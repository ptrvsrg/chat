package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.XmlUtils;

@RequiredArgsConstructor
public class XmlFileConnection
    implements Connection {

    private final BufferedReader in;
    private final PrintWriter out;

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
            out.println(xml);
        }
    }

    @Override
    public DTO receive()
        throws IOException {
        String xml;
        synchronized (in) {
            xml = in.readLine();
        }

        try {
            return XmlUtils.xmlToDto(xml);
        } catch (JAXBException e) {
            throw new IOException("Invalid DTO format : " + e);
        }
    }
}