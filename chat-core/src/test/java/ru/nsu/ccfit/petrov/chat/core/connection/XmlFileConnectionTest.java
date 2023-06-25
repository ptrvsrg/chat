package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.bind.JAXBException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.XmlUtils;

@RunWith(MockitoJUnitRunner.class)
public class XmlFileConnectionTest
    extends Assertions {

    private final BufferedReader in = Mockito.mock(BufferedReader.class);
    private final PrintWriter out = Mockito.mock(PrintWriter.class);
    private final XmlFileConnection connection = new XmlFileConnection(in, out);

    @Test
    public void send_noException() {
        Mockito.doNothing()
               .when(out)
               .println(Mockito.any(String.class));

        assertThatNoException().isThrownBy(() -> connection.send(new DTO()));
    }

    @Test
    public void send_dtoToXmlThrowJAXBException_thrownIOException() {
        try (MockedStatic<XmlUtils> xmlUtils = Mockito.mockStatic(XmlUtils.class)) {
            xmlUtils.when(() -> XmlUtils.dtoToXml(Mockito.any(DTO.class)))
                    .thenThrow(JAXBException.class);

            assertThatIOException().isThrownBy(() -> connection.send(new DTO()))
                                   .withMessageStartingWith("Invalid DTO format :");
        }
    }

    @Test
    public void receive_noException()
        throws IOException, JAXBException {
        DTO expectedDto = new DTO();
        Mockito.when(in.readLine())
               .thenReturn(XmlUtils.dtoToXml(expectedDto));

        assertThatNoException().isThrownBy(
            () -> assertThat(connection.receive()).isEqualTo(expectedDto));
    }

    @Test
    public void receive_xmlToDtoThrowJAXBException_thrownIOException()
        throws IOException {
        Mockito.when(in.readLine())
               .thenReturn("");

        try (MockedStatic<XmlUtils> xmlUtils = Mockito.mockStatic(XmlUtils.class)) {
            xmlUtils.when(() -> XmlUtils.xmlToDto(Mockito.any(String.class)))
                    .thenThrow(JAXBException.class);

            assertThatIOException().isThrownBy(connection::receive)
                                   .withMessageStartingWith("Invalid DTO format :");
        }
    }
}