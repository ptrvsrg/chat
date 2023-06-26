package ru.nsu.ccfit.petrov.chat.server.database;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import ru.nsu.ccfit.petrov.chat.core.connection.Connection;
import ru.nsu.ccfit.petrov.chat.core.connection.XmlFileConnection;

public class UserRepositoryInMemoryTest
    extends Assertions {

    private Set<User> users;
    private final UserRepositoryInMemory userRepository = new UserRepositoryInMemory();

    @Before
    public void setUp()
        throws NoSuchFieldException, IllegalAccessException {
        users = new HashSet<>();

        Field usersField = UserRepositoryInMemory.class.getDeclaredField("users");
        usersField.setAccessible(true);
        usersField.set(userRepository, users);
    }

    @Test
    public void addUser_uniqueTwoUsers_onlyTwoUsersInRepository() {
        User user1 = new User("User1", null);
        User user2 = new User("User2", null);

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        assertThat(users).hasSize(2);
    }

    @Test
    public void addUser_sameTwoUsers_onlyOneUserInRepository() {
        User user1 = new User("User", null);
        User user2 = new User("User", null);

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        assertThat(users).hasSize(1);
    }

    @Test
    public void findUserByUsername_requiredUserExists_requiredUser() {
        String username = "User1";
        User user = new User(username, null);

        userRepository.addUser(user);

        assertThat(userRepository.findUserByUsername(username)).isEqualTo(user);
    }

    @Test
    public void findUserByUsername_requiredUserDoesNotExist_null() {
        String username = "User1";

        assertThat(userRepository.findUserByUsername(username)).isNull();
    }

    @Test
    public void findUserByConnection_requiredUserExists_requiredUser() {
        Connection connection = new XmlFileConnection();
        User user = new User("User", connection);

        userRepository.addUser(user);

        assertThat(userRepository.findUserByConnection(connection)).isEqualTo(user);
    }

    @Test
    public void findUserByConnection_requiredUserDoesNotExist_null() {
        Connection connection = new XmlFileConnection();

        assertThat(userRepository.findUserByConnection(connection)).isNull();
    }

    @Test
    public void getUsernames_userRepositoryIsNotEmpty_usernameArray() {
        String[] usernames = {
            "User1",
            "User2",
            "User3",
            "User4",
            "User5",
            };

        Arrays.stream(usernames)
              .forEach(username -> userRepository.addUser(new User(username, null)));

        assertThat(userRepository.getUsernames()).containsAll(Arrays.asList(usernames));
    }

    @Test
    public void getUsernames_userRepositoryIsEmpty_emptyUsernameArray() {
        assertThat(userRepository.getUsernames()).isEmpty();
    }

    @Test
    public void getConnections_userRepositoryIsNotEmpty_connectionArray() {
        Connection[] connections = {
            new XmlFileConnection(),
            new XmlFileConnection(),
            new XmlFileConnection()
        };

        Arrays.stream(connections)
              .forEach(connection -> userRepository.addUser(new User("", connection)));

        assertThat(userRepository.getConnections()).containsAll(Arrays.asList(connections));
    }

    @Test
    public void getConnections_userRepositoryIsEmpty_EmptyConnectionArray() {
        assertThat(userRepository.getConnections()).isEmpty();
    }

    @Test
    public void getUsers_userRepositoryIsNotEmpty_userSet() {
        User[] users = {
            new User("User1", new XmlFileConnection()),
            new User("User2", new XmlFileConnection()),
            new User("User3", new XmlFileConnection())
        };

        Arrays.stream(users)
              .forEach(userRepository::addUser);

        assertThat(userRepository.getUsers()).containsAll(Arrays.asList(users));
    }

    @Test
    public void getUsers_userRepositoryIsEmpty_EmptyUserSet() {
        assertThat(userRepository.getUsers()).isEmpty();
    }

    @Test
    public void removeUser_userRepositoryIsNotEmpty() {
        User user1 = new User("User1", null);

        userRepository.addUser(user1);
        assertThat(users).hasSize(1);

        userRepository.removeUser(user1.getUsername());
        assertThat(users).isEmpty();
    }

    @Test
    public void removeUser_userRepositoryIsEmpty() {
        User user1 = new User("User1", null);
        assertThatNoException().isThrownBy(() -> userRepository.removeUser(user1.getUsername()));
        assertThat(users).isEmpty();
    }

    @Test
    public void removeUser_requiredUserDoesNotExist() {
        User user1 = new User("User1", null);
        User user2 = new User("User2", null);

        userRepository.addUser(user1);
        assertThat(users).hasSize(1);

        assertThatNoException().isThrownBy(() -> userRepository.removeUser(user2.getUsername()));
        assertThat(users).hasSize(1);
    }
}