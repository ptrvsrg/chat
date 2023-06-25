package ru.nsu.ccfit.petrov.chat.core.dto;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Subtype;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Type;

public class XmlUtilsTest
    extends Assertions {

    private static final UUID expectedId = UUID.fromString("be4f6d29-a4ec-4a05-980e-4823d039d70c");
    private static final Type expectedType = Type.REQUEST;
    private static final Subtype expectedSubtype = Subtype.LOGIN;
    private static final UUID expectedRequestId = UUID.fromString("5c6e1a7b-736e-416c-bbf1-b260988c0b0d");
    private static final String expectedUsername = "User1";
    private static final String expectedMessage = "Hello, World!";
    private static final String[] expectedUsers = new String[] {
        "User2",
        "User3",
        "User4"
    };
    private static final String expectedXmlFile =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
        "<dto id=\"be4f6d29-a4ec-4a05-980e-4823d039d70c\" type=\"REQUEST\" subtype=\"LOGIN\">\n" +
        "    <requestId>5c6e1a7b-736e-416c-bbf1-b260988c0b0d</requestId>\n" +
        "    <username>User1</username>\n" +
        "    <message>Hello, World!</message>\n" +
        "    <users>\n" +
        "        <user>User2</user>\n" +
        "        <user>User3</user>\n" +
        "        <user>User4</user>\n" +
        "    </users>\n" +
        "</dto>\n";
    private final DTO dto = new DTO(expectedId, expectedType, expectedSubtype, expectedRequestId,
                                    expectedUsername, expectedMessage, expectedUsers);

    @Test
    public void xmlToDto() {
        assertThatNoException().isThrownBy(
            () -> assertThat(XmlUtils.xmlToDto(expectedXmlFile)).isEqualTo(dto));
    }

    @Test
    public void dtoToXml() {
        assertThatNoException().isThrownBy(
            () -> assertThat(XmlUtils.dtoToXml(dto)).isEqualTo(expectedXmlFile));
    }
}