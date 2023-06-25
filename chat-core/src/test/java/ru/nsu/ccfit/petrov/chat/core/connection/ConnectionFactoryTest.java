package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.net.Socket;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

public class ConnectionFactoryTest
    extends Assertions {

    private final Socket clientSocket = Mockito.mock(Socket.class);

    @Test
    public void newConnection_javaObjectDtoType_javaObjectConnection()
        throws IOException {
        // prepare
        byte[] buffer = new byte[] {
            (byte) ((ObjectStreamConstants.STREAM_MAGIC & 0xFF00) >> 8),
            (byte) (ObjectStreamConstants.STREAM_MAGIC & 0x00FF),
            (byte) ((ObjectStreamConstants.STREAM_VERSION & 0xFF00) >> 8),
            (byte) (ObjectStreamConstants.STREAM_VERSION & 0x00FF)
        };
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.write(buffer);
        objectOutputStream.flush();

        ObjectInputStream objectInputStream = new ObjectInputStream(
            new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        Mockito.when(clientSocket.getInputStream())
               .thenReturn(objectInputStream);
        Mockito.when(clientSocket.getOutputStream())
               .thenReturn(objectOutputStream);

        // do and check
        assertThat(
            ConnectionFactory.newConnection(DTOFormat.JAVA_OBJECT, clientSocket)).isInstanceOf(
            JavaObjectConnection.class);
    }

    @Test
    public void newConnection_xmlFileDtoType_xmlFileConnection()
        throws IOException {
        // prepare
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
            byteArrayOutputStream.toByteArray());

        Mockito.when(clientSocket.getInputStream())
               .thenReturn(byteArrayInputStream);
        Mockito.when(clientSocket.getOutputStream())
               .thenReturn(byteArrayOutputStream);

        // do and check
        assertThat(ConnectionFactory.newConnection(DTOFormat.XML_FILE, clientSocket)).isInstanceOf(
            XmlFileConnection.class);
    }

    @Test
    public void newConnection_clientSocketThrowIOException_thrownIOException()
        throws IOException {
        Mockito.when(clientSocket.getInputStream())
               .thenThrow(new IOException());
        Mockito.when(clientSocket.getOutputStream())
               .thenThrow(new IOException());

        assertThatIOException().isThrownBy(
                                   () -> ConnectionFactory.newConnection(DTOFormat.JAVA_OBJECT, clientSocket));
    }
}