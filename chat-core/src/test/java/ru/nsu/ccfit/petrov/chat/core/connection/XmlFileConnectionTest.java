package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.Socket;
import javax.xml.bind.JAXBException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
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

    private BufferedReader in;
    private BufferedWriter out;
    private final XmlFileConnection connection = new XmlFileConnection();

    @Before
    public void setUp()
        throws NoSuchFieldException, IllegalAccessException {
        in = Mockito.spy(
            new BufferedReader(new InputStreamReader(Mockito.mock(InputStream.class))));
        out = Mockito.spy(
            new BufferedWriter(new OutputStreamWriter(Mockito.mock(OutputStream.class))));

        setFieldValue("in", connection, in);
        setFieldValue("out", connection, out);
    }

    private void setFieldValue(String fieldName, Object object, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = XmlFileConnection.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    public void connect_socketReturnCorrectStreams()
        throws IOException {
        // prepare
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
            byteArrayOutputStream.toByteArray());

        // do
        try (Socket clientSocket = Mockito.mock(Socket.class)) {
            Mockito.when(clientSocket.getInputStream())
                   .thenReturn(byteArrayInputStream);
            Mockito.when(clientSocket.getOutputStream())
                   .thenReturn(byteArrayOutputStream);
            assertThatNoException().isThrownBy(() -> connection.connect(clientSocket));
        }
    }

    @Test
    public void connect_socketThrowsIOException_thrownIOException()
        throws IOException {
        try (Socket clientSocket = Mockito.mock(Socket.class)) {
            Mockito.when(clientSocket.getInputStream())
                   .thenThrow(new IOException());
            assertThatIOException().isThrownBy(() -> connection.connect(clientSocket));
        }
    }

    @Test
    public void send_outIsNull_thrownIOException()
        throws NoSuchFieldException, IllegalAccessException {
        setFieldValue("out", connection, null);

        assertThatIOException().isThrownBy(() -> connection.send(new DTO()))
                               .withMessage("Connection not established");
    }

    @Test
    public void send_noException()
        throws IOException {
        Mockito.doNothing()
               .when(out)
               .write(Mockito.any(String.class));

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
    public void receive_inIsNull_thrownIOException()
        throws NoSuchFieldException, IllegalAccessException {
        setFieldValue("in", connection, null);

        assertThatIOException().isThrownBy(connection::receive)
                               .withMessage("Connection not established");
    }

    @Test
    public void receive_noException()
        throws NoSuchFieldException, IllegalAccessException, JAXBException {
        DTO expectedDto = new DTO();
        String message = XmlUtils.dtoToXml(expectedDto) + "\n";
        setFieldValue("in", connection,
                      new BufferedReader(new StringReader(message)));

        assertThatNoException().isThrownBy(
            () -> assertThat(connection.receive()).isEqualTo(expectedDto));
    }

    @Test
    public void receive_xmlToDtoThrowJAXBException_thrownIOException()
        throws IOException {
        Mockito.doReturn("")
               .when(in)
               .readLine();

        try (MockedStatic<XmlUtils> xmlUtils = Mockito.mockStatic(XmlUtils.class)) {
            xmlUtils.when(() -> XmlUtils.xmlToDto(Mockito.any(String.class)))
                    .thenThrow(JAXBException.class);

            assertThatIOException().isThrownBy(connection::receive)
                                   .withMessageStartingWith("Invalid DTO format :");
        }
    }
}