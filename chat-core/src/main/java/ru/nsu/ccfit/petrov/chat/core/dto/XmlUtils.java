package ru.nsu.ccfit.petrov.chat.core.dto;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtils {

    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    private XmlUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static DTO xmlToDto(String xml)
        throws JAXBException {
        if (unmarshaller == null) {
            JAXBContext jaxbContext = JAXBContext.newInstance(DTO.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        }

        StringReader stringReader = new StringReader(xml);
        return (DTO) unmarshaller.unmarshal(stringReader);
    }

    public static String dtoToXml(DTO dto)
        throws JAXBException {
        if (marshaller == null) {
            JAXBContext jaxbContext = JAXBContext.newInstance(DTO.class);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(dto, stringWriter);
        return stringWriter.toString();
    }
}
