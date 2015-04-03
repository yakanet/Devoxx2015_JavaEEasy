package fr.devoxx.javaeeasy.services.business;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

@ApplicationScoped
public class MessageBroadCaster {
    @Inject
    private Logger logger;

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void register(final Session s) {
        sessions.put(s.getId(), s);
    }

    public void deregister(final Session s) {
        sessions.remove(s.getId());
    }

    public void broadcast(final Object message) {
        sessions.values().stream().forEach((s) -> doSend(message, s));
    }

    private boolean doSend(final Object message, final Session s) {
        if (!s.isOpen()) {
            //TODO Should we remove it from the map ?
            return true;
        }
        try {
            s.getBasicRemote().sendObject(message);
        } catch (final Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return false;
    }
}
