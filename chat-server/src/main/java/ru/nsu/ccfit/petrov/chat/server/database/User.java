package ru.nsu.ccfit.petrov.chat.server.database;

import lombok.Value;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;

/**
 * The type User is class that describes entity about user that is stored in repository.
 *
 * @author ptrvsrg
 */
@Value
public class User {

    String username;
    Connection connection;
}
