package ru.nsu.ccfit.petrov.chat.core.connection;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;

public class ConnectionFactoryTest
    extends Assertions {

    @Test
    public void newConnection_javaObjectDtoType_javaObjectConnection() {
        assertThat(ConnectionFactory.newConnection(DTOFormat.JAVA_OBJECT)).isInstanceOf(
            JavaObjectConnection.class);
    }

    @Test
    public void newConnection_xmlFileDtoType_xmlFileConnection() {
        assertThat(ConnectionFactory.newConnection(DTOFormat.XML_FILE)).isInstanceOf(
            XmlFileConnection.class);
    }
}