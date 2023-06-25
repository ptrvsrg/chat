package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;

@RunWith(MockitoJUnitRunner.class)
public class JavaObjectConnectionTest
    extends Assertions {

    private final ObjectInputStream in = Mockito.spy(ObjectInputStream.class);
    private final ObjectOutputStream out = Mockito.spy(ObjectOutputStream.class);
    private final JavaObjectConnection connection = new JavaObjectConnection(in, out);

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