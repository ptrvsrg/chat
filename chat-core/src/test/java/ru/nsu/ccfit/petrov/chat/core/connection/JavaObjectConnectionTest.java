package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.lang.reflect.Field;
import java.net.Socket;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

@RunWith(MockitoJUnitRunner.class)
public class JavaObjectConnectionTest
    extends Assertions {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final JavaObjectConnection connection = new JavaObjectConnection();

    @Before
    public void setUp()
        throws NoSuchFieldException, IllegalAccessException {
        in = Mockito.spy(ObjectInputStream.class);
        out = Mockito.spy(ObjectOutputStream.class);

        setFieldValue("in", connection, in);
        setFieldValue("out", connection, out);
    }

    private void setFieldValue(String fieldName, Object object, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = JavaObjectConnection.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    public void connect_socketReturnCorrectStreams()
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

        // do
        try (Socket clientSocket = Mockito.mock(Socket.class)) {
            Mockito.when(clientSocket.getInputStream()).thenReturn(objectInputStream);
            Mockito.when(clientSocket.getOutputStream()).thenReturn(objectOutputStream);
            assertThatNoException().isThrownBy(() -> connection.connect(clientSocket));
        }
    }

    @Test
    public void connect_socketThrowsIOException_thrownIOException()
        throws IOException {
        try (Socket clientSocket = Mockito.mock(Socket.class)) {
            Mockito.when(clientSocket.getInputStream()).thenThrow(new IOException());
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
               .writeObject(Mockito.any(DTO.class));

        assertThatNoException().isThrownBy(() -> connection.send(new DTO()));
    }

    @Test
    public void send_writeObjectThrowIOException_thrownIOException()
        throws IOException {
        Mockito.doThrow(new IOException())
               .when(out)
               .writeObject(Mockito.any(DTO.class));

        assertThatIOException().isThrownBy(() -> connection.send(new DTO()));
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
        throws IOException, ClassNotFoundException {
        DTO expectedDto = new DTO();
        Mockito.when(in.readObject())
               .thenReturn(expectedDto);

        assertThatNoException().isThrownBy(
            () -> assertThat(connection.receive()).isEqualTo(expectedDto));
    }

    @Test
    public void receive_readObjectThrowIOException_thrownIOException()
        throws IOException, ClassNotFoundException {
        Mockito.doThrow(new IOException())
               .when(in)
               .readObject();

        assertThatIOException().isThrownBy(connection::receive);
    }

    @Test
    public void receive_readObjectThrowClassNotFoundException_thrownIOException()
        throws IOException, ClassNotFoundException {
        Mockito.doThrow(new ClassNotFoundException())
               .when(in)
               .readObject();

        assertThatIOException().isThrownBy(connection::receive)
                               .withMessageStartingWith("Invalid DTO format :");
    }

    @Test
    public void receive_readObjectReturnInteger_thrownIOException()
        throws IOException, ClassNotFoundException {
        Mockito.doReturn(2)
               .when(in)
               .readObject();

        assertThatIOException().isThrownBy(connection::receive)
                               .withMessageStartingWith("Invalid DTO format :");
    }
}