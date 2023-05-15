package ru.nsu.ccfit.petrov.task5.server.database;

import java.util.Map.Entry;
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

    public String getUserByConnection(Connection connection) {
        for (Entry<String, Connection> entry : users.entrySet()) {
            if (entry.getValue() == connection) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void removeUser(String userName) {
        users.remove(userName);
    }

    public void removeUser(Connection connection) {
        for (Entry<String, Connection> entry : users.entrySet()) {
            if (entry.getValue() == connection) {
                users.remove(entry.getKey());
            }
        }
    }

}