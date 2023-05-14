package ru.nsu.ccfit.petrov.task5.server.database;

import lombok.Getter;
import ru.nsu.ccfit.petrov.task5.connection.Connection;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ServerDB {
    private final Map<String, Connection> users = new HashMap<>();

    public void addUser(String userName, Connection connection) {
        users.put(userName, connection);
    }

    public void removeUser(String userName) {
        users.remove(userName);
    }

}