package ru.nsu.ccfit.petrov.chat.core.dto;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Subtype;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO.Type;

public class DTOTest
    extends Assertions {

    private static final UUID expectedId = UUID.randomUUID();
    private static final Type expectedType = Type.REQUEST;
    private static final Subtype expectedSubtype = Subtype.LOGIN;
    private static final UUID expectedRequestId = UUID.randomUUID();
    private static final String expectedUsername = "User1";
    private static final String expectedMessage = "Hello, World!";
    private static final String[] expectedUsers = new String[] {
        "User2",
        "User3",
        "User4"
    };
    private final DTO dto = new DTO(expectedId, expectedType, expectedSubtype, expectedRequestId,
                                    expectedUsername, expectedMessage, expectedUsers);

    @Test
    public void getId() {
        assertThat(dto.getId()).isEqualTo(expectedId);
    }

    @Test
    public void getType() {
        assertThat(dto.getType()).isEqualTo(expectedType);
    }

    @Test
    public void getSubtype() {
        assertThat(dto.getSubtype()).isEqualTo(expectedSubtype);
    }

    @Test
    public void getRequestId() {
        assertThat(dto.getRequestId()).isEqualTo(expectedRequestId);
    }

    @Test
    public void getUsername() {
        assertThat(dto.getUsername()).isEqualTo(expectedUsername);
    }

    @Test
    public void getMessage() {
        assertThat(dto.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void getUsers() {
        assertThat(dto.getUsers()).isEqualTo(expectedUsers);
    }
}