package ru.nsu.ccfit.petrov.chat.core.dto;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * The type XmlUtils is utility class that contains methods for converting DTO and XML files.
 *
 * @author ptrvsrg
 */
public class XmlUtils {

    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    private XmlUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Convert XML file to DTO.
     *
     * @param xml the XML file as string
     * @return the DTO
     * @throws JAXBException If XML file is invalid and does not fit DTO pattern
     */
    public static DTO xmlToDto(String xml)
        throws JAXBException {
        if (unmarshaller == null) {
            JAXBContext jaxbContext = JAXBContext.newInstance(DTO.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        }

        StringReader stringReader = new StringReader(xml);
        return (DTO) unmarshaller.unmarshal(stringReader);
    }

    /**
     * Convert DTO to XML file.
     *
     * @param dto the DTO
     * @return the XML file as string
     * @throws JAXBException If DTO is invalid
     */
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
