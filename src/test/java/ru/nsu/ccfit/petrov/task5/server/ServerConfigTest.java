package ru.nsu.ccfit.petrov.task5.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServerConfigTest
    extends Assertions {

    private Properties properties;

    private void setProperties(Properties properties)
        throws NoSuchFieldException, IllegalAccessException {
        Field propertiesField = ServerConfig.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);

        Field propertiesModifiers = propertiesField.getClass().getDeclaredField("modifiers");
        propertiesModifiers.setAccessible(true);
        propertiesModifiers.setInt(propertiesField, propertiesField.getModifiers() & ~Modifier.FINAL);

        propertiesField.set(null, properties);
    }

    @BeforeClass
    public void beforeClass()
        throws NoSuchFieldException, IllegalAccessException {
        properties = Mockito.mock(Properties.class);
        setProperties(properties);
    }

    @Test(description = "Check getting port",
          groups = "Server config tests")
    public void checkGetPort() {
        // prepare
        int expectedPort = 5000;
        Mockito.when(properties.getProperty("connection.port")).thenReturn(Integer.toString(expectedPort));

        // do
        int actualPort = ServerConfig.getPort();

        // check
        assertThat(actualPort).isEqualTo(expectedPort);
    }

    @Test(description = "Check getting port when property value is not Integer",
          groups = "Server config tests")
    public void checkGetPortWhenPropertyValueIsNotInteger() {
        // prepare
        Mockito.when(properties.getProperty("connection.port")).thenReturn("Hello");

        // check
        assertThatThrownBy(ServerConfig::getPort).isInstanceOf(NumberFormatException.class);
    }

    @Test(description = "Check getting timeout",
          groups = "Server config tests")
    public void checkGetTimeout() {
        // prepare
        int expectedTimeout = 60000;
        Mockito.when(properties.getProperty("session.timeout")).thenReturn(Integer.toString(expectedTimeout));

        // do
        int actualTimeout = ServerConfig.getTimeout();

        // check
        assertThat(actualTimeout).isEqualTo(expectedTimeout);
    }

    @Test(description = "Check getting timeout when property value is not Integer",
          groups = "Server config tests")
    public void checkGetTimeoutWhenPropertyValueIsNotInteger() {
        // prepare
        Mockito.when(properties.getProperty("session.timeout")).thenReturn("Hello");

        // check
        assertThatThrownBy(ServerConfig::getTimeout).isInstanceOf(NumberFormatException.class);
    }

    @Test(description = "Check getting message type",
          groups = "Server config tests")
    public void checkGetMessageType() {
        // prepare
        String expectedMessageType = "xml";
        Mockito.when(properties.getProperty("message.type")).thenReturn(expectedMessageType);

        // do
        String actualMessageType = ServerConfig.getMessageType();

        // check
        assertThat(actualMessageType).isEqualTo(expectedMessageType);
    }
}