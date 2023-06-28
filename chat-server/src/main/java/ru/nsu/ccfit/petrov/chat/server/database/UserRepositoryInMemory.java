package ru.nsu.ccfit.petrov.chat.server.database;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;

/**
 * The type UserRepositoryInMemory is class that implements {@link UserRepository} for storing data in memory.
 */
@Getter
public class UserRepositoryInMemory
    implements UserRepository {

    private final Set<User> users = new HashSet<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User findUserByConnection(Connection connection) {
        for (User user : users) {
            if (Objects.equals(user.getConnection(), connection)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public String[] getUsernames() {
        return users.stream()
                    .map(User::getUsername)
                    .collect(Collectors.toSet())
                    .toArray(String[]::new);
    }

    @Override
    public Connection[] getConnections() {
        return users.stream()
                    .map(User::getConnection)
                    .collect(Collectors.toSet())
                    .toArray(Connection[]::new);
    }

    @Override
    public void removeUser(String username) {
        User user = findUserByUsername(username);

        if (user != null) {
            users.remove(user);
        }
    }
}