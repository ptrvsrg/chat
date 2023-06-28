package ru.nsu.ccfit.petrov.chat.server.database;

import ru.nsu.ccfit.petrov.chat.core.connection.Connection;

/**
 * The interface UserRepository is interface that describes methods for interacting with the user store.
 *
 * @author ptrvsrg
 */
public interface UserRepository {

    /**
     * Add user entity.
     *
     * @param user the user
     */
    void addUser(User user);

    /**
     * Find user by username.
     *
     * @param username the username
     * @return the user
     */
    User findUserByUsername(String username);

    /**
     * Find user by connection.
     *
     * @param connection the connection
     * @return the user
     */
    User findUserByConnection(Connection connection);

    /**
     * Get usernames.
     *
     * @return the usernames array
     */
    String[] getUsernames();

    /**
     * Get connections.
     *
     * @return the connection array
     */
    Connection[] getConnections();

    /**
     * Remove user by username.
     *
     * @param username the username
     */
    void removeUser(String username);

}