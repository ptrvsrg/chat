package ru.nsu.ccfit.petrov.chat.server.config;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

@RunWith(Parameterized.class)
public class ServerConfigTest
    extends Assertions {

    private final Properties testProperties = Mockito.mock(Properties.class);

    private final String dtoFormatProperty;

    private final DTOFormat dtoFormat;

    public ServerConfigTest(String dtoFormatProperty, DTOFormat dtoFormat) {
        this.dtoFormatProperty = dtoFormatProperty;
        this.dtoFormat = dtoFormat;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                "java_object",
                DTOFormat.JAVA_OBJECT
            },
            {
                "xml_file",
                DTOFormat.XML_FILE
            }
        });
    }

    @Before
    public void before()
        throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(ServerConfigTest.class);

        Field propertiesField = ServerConfig.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        propertiesField.set(ServerConfig.class, testProperties);
    }

    @Test
    public void getAddress_propertiesWithCorrectAddress_noException() {
        Mockito.when(testProperties.getProperty("connection.address"))
               .thenReturn("localhost");

        assertThatNoException().isThrownBy(() -> assertThat(ServerConfig.getAddress()).isEqualTo(
            InetAddress.getByName("localhost")));
    }

    @Test
    public void getAddress_propertiesWithIncorrectAddress_thrownUnknownHostException() {
        Mockito.when(testProperties.getProperty("connection.address"))
               .thenReturn("127.0.0.256");

        assertThatExceptionOfType(UnknownHostException.class).isThrownBy(ServerConfig::getAddress);
    }

    @Test
    public void getPort_propertiesWithCorrectNumber_noException() {
        Mockito.when(testProperties.getProperty("connection.port"))
               .thenReturn("5000");

        assertThatNoException().isThrownBy(
            () -> assertThat(ServerConfig.getPort()).isEqualTo(5000));
    }

    @Test
    public void getPort_propertiesWithIncorrectNumber_thrownNumberFormatException() {
        Mockito.when(testProperties.getProperty("connection.port"))
               .thenReturn("Hello");

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(ServerConfig::getPort);
    }

    @Test
    public void getTimeout_propertiesWithCorrectNumber_noException() {
        Mockito.when(testProperties.getProperty("connection.timeout"))
               .thenReturn("5000");

        assertThatNoException().isThrownBy(
            () -> assertThat(ServerConfig.getTimeout()).isEqualTo(5000));
    }

    @Test
    public void getTimeout_propertiesWithIncorrectNumber_thrownNumberFormatException() {
        Mockito.when(testProperties.getProperty("connection.timeout"))
               .thenReturn("Hello");

        assertThatExceptionOfType(NumberFormatException.class).isThrownBy(ServerConfig::getTimeout);
    }

    @Test
    public void getDTOFormat_propertiesWithCorrectDTOFormat_noException() {
        Mockito.when(testProperties.getProperty("DTO.format"))
               .thenReturn(dtoFormatProperty);

        assertThatNoException().isThrownBy(
            () -> assertThat(ServerConfig.getDTOFormat()).isEqualTo(dtoFormat));
    }

    @Test
    public void getDTOFormat_propertiesWithIncorrectDTOFormat_thrownIllegalStateException() {
        Mockito.when(testProperties.getProperty("DTO.format"))
               .thenReturn("protobuf");

        assertThatIllegalStateException().isThrownBy(ServerConfig::getDTOFormat);
    }
}