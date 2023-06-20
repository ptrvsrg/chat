package ru.nsu.ccfit.petrov.task5.server.database;

import ru.nsu.ccfit.petrov.task5.connection.Connection;

public interface UserRepository {

    void addUser(User user);

    User findUserByUsername(String username);

    User findUserByConnection(Connection connection);

    String[] getUsernames();

    Connection[] getConnections();

    void removeUser(String username);

}