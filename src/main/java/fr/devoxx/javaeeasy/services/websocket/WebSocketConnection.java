package fr.devoxx.javaeeasy.services.websocket;

import fr.devoxx.javaeeasy.services.business.MessageBroadCaster;

import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@Dependent
@ServerEndpoint(value = "/ws/planning", encoders = JsonEncoder.class)
public class WebSocketConnection {
    @Inject
    private MessageBroadCaster messageBroadcaster;

    @Inject
    private Logger logger;
    
    @OnOpen
    public void onOpen(final Session session) {
        messageBroadcaster.register(session);
    }
    
    @OnError
    public void onError(final Throwable throwable) {
    	logger.throwing("WebSocketConnection", "onError", throwable);
    }

    @OnClose
    public void onClose(final Session session) {
        messageBroadcaster.deregister(session);
    }
}
