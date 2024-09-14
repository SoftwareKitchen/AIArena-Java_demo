package de.aiarena.demo.websocket;

@FunctionalInterface
public interface AIArenaWebSocketMessageHandler {
    void handleMessage(String message);
}

