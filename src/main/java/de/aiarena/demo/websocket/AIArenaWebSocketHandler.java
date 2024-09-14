package de.aiarena.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;

public class AIArenaWebSocketHandler implements WebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private WebSocketSession session = null;
    private final AIArenaWebSocketMessageHandler messageHandler;

    public AIArenaWebSocketHandler(AIArenaWebSocketMessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public void send(String message) {
        logger.info("SEND > " + message);
        try {
            session.sendMessage(new TextMessage(message));
        }catch(Exception ex){
            logger.warn("Unable to send message", ex);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(this.session != null){
            logger.warn("Replacing existing connection!");
        }
        logger.info("Session opened");
        this.session = session;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("RECV < "+message.getPayload().toString());
        messageHandler.handleMessage(message.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Session Error, shutting down");
        System.exit(1);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.warn("Connection closed, shutting down");
        System.exit(1);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
