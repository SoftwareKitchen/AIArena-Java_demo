package de.aiarena.demo.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aiarena.demo.ai.AI;
import de.aiarena.demo.messages.CommonMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;

@Component
public class AIArenaWebSocketClient {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private boolean authenticated = false;
    private final ObjectMapper mapper;
    private final AIArenaWebSocketHandler sessionHandler;
    private final AI availableAI;
    private boolean gameActive = false;

    @Autowired
    public AIArenaWebSocketClient(
            ObjectMapper mapper,
            AI availableAI
    ){
        this.mapper = mapper;
        var client = new StandardWebSocketClient();
        sessionHandler = new AIArenaWebSocketHandler(this::onMessage);
        this.availableAI = availableAI;
        try {
            client.execute(sessionHandler, new WebSocketHttpHeaders(), new URI("ws://localhost/api/live"));//("wss://ai-arena.de/api/live"));
        }catch(Exception ex){
            logger.error("Error starting WebSocket connection");
            System.exit(1);
        }
        availableAI.provideSink(sessionHandler::send);
    }

    @Scheduled(fixedDelay = 10000)
    private void searchGames(){
        if(!authenticated){
            logger.warn("Not authenticated, skipping search");
            return;
        }

        if(gameActive){
            logger.info("Skipping match list search - game running");
            return;
        }

        try {
            matchList(availableAI.getName());
        }catch(Exception ex){
            logger.warn("Unable to send match list request ",ex);
        }
    }

    private void onMessage(String message){
        CommonMessages.GenericInboundMessage genericParse;
        try{
            genericParse = mapper.readValue(message, CommonMessages.GenericInboundMessage.class);
        }catch(Exception ex){
            logger.warn("Unable to parse inbound message "+message);
            logger.warn(ex.toString());
            ex.printStackTrace();
            return;
        }

        switch(genericParse.type){
            case "identified":
                logger.info("Authentication successful");
                authenticated = true;
                break;
            case "match-end":
                gameActive = false;
                break;
            case "open-lobbies":
                try {
                    var parsed = mapper.readValue(message, CommonMessages.OpenLobbiesMessage.class);
                    if(parsed.matches.isEmpty()){
                        logger.info("No game found");
                    }else{
                        logger.info("Game found, connecting");
                        join(parsed.matches.get(0).match);
                        gameActive = true;
                    }

                }catch(Exception ex){
                    logger.warn("Unable to parse match list message ", ex);
                }
                break;
            default:
                logger.warn("Unknown message type, sending to AI: "+genericParse.type);
                availableAI.handleMessage(message);
        }
    }

    public void matchList(String game) throws IOException {
        if(!authenticated){
            logger.warn("Unable to request match list before successful authentication");
            return;
        }

        var message = new CommonMessages.MatchListMessage(game);
        sessionHandler.send(mapper.writeValueAsString(message));
    }

    public void join(String matchId) throws IOException {
        if(!authenticated){
            logger.warn("Unable to join before successful authentication");
            return;
        }

        var message = new CommonMessages.JoinMessage(matchId);
        sessionHandler.send(mapper.writeValueAsString(message));
    }

    public void authenticate(String token) throws IOException {
        var message = new CommonMessages.AuthenticateMessage(token);
        sessionHandler.send(mapper.writeValueAsString(message));
    }
}
