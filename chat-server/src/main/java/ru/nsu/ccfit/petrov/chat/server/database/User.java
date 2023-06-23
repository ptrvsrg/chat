package ru.nsu.ccfit.petrov.chat.server.database;

import lombok.Value;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;

@Value
public class User {

    String username;
    Connection connection;
}
