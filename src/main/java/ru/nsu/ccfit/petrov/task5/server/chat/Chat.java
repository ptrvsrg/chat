package ru.nsu.ccfit.petrov.task5.server.chat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.server.session.Session;

/**
 * The type {@code Chat} is class that describes chat, contains client sessions.
 *
 * @author ptrvsrg
 */
@RequiredArgsConstructor
public class Chat {

    @Getter private final String name;
    @Getter private final Set<Session> sessions = new HashSet<>();

    /**
     * Adds session.
     *
     * @param session the session
     */
    public void addSession(Session session) {
        sessions.add(session);
    }

    /**
     * Removes session by id.
     *
     * @param sessionId the session id
     */
    public void removeSessionById(UUID sessionId) {
        for (Session session : sessions) {
            if (session.getId() == sessionId) {
                session.close();
                sessions.remove(session);
                return;
            }
        }
    }

    /**
     * Gets session by id.
     *
     * @param sessionId the session id
     * @return the session by id
     */
    public Session getSessionById(UUID sessionId) {
        for (Session session : sessions) {
            if (session.getId() == sessionId) {
                return session;
            }
        }

        return null;
    }

    /**
     * Gets session ids.
     *
     * @return the session ids
     */
    public Set<UUID> getSessionIds() {
        Set<UUID> sessionIds = new HashSet<>();
        for (Session session : sessions) {
            sessionIds.add(session.getId());
        }

        return sessionIds;
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public Set<String> getUsers() {
        Set<String> users = new HashSet<>();
        for (Session session : sessions) {
            users.add(session.getUserName());
        }

        return users;
    }
}
