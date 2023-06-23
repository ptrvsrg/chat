package ru.nsu.ccfit.petrov.chat.server.database;

import ru.nsu.ccfit.petrov.chat.core.connection.Connection;

public interface UserRepository {

    void addUser(User user);

    User findUserByUsername(String username);

    User findUserByConnection(Connection connection);

    String[] getUsernames();

    Connection[] getConnections();

    void removeUser(String username);

}