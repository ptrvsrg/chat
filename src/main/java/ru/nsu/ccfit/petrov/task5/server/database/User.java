package ru.nsu.ccfit.petrov.task5.server.database;

import lombok.Value;
import ru.nsu.ccfit.petrov.task5.connection.Connection;

@Value
public class User {

    String username;
    Connection connection;
}
