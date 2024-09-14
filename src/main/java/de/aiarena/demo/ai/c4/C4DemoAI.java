package de.aiarena.demo.ai.c4;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aiarena.demo.ai.AI;
import de.aiarena.demo.messages.CommonMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class C4DemoAI implements AI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectMapper mapper;
    private AICommandSink acs;

    @Autowired
    public C4DemoAI(ObjectMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public String getName() {
        return "Connect Four";
    }

    @Override
    public void handleMessage(String message) {
        try {
            var preParsed = mapper.readValue(message, CommonMessages.GenericInboundMessage.class);
            switch(preParsed.type){
                case "game-update":
                    var parsedMessage = mapper.readValue(message, ConnectFourMessages.ConnectFourGameUpdate.class);
                    if(parsedMessage.activePlayer != null && parsedMessage.slot != null && parsedMessage.activePlayer.equals(parsedMessage.slot)){
                        theActualAIMethod(parsedMessage.state);
                    }

                    break;
                default:
                    logger.warn("Unhandled keyword: "+preParsed.type);
            }


        }catch(Exception ex){
            logger.warn("Unable to parse message: "+message);
            logger.warn(ex.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public void provideSink(AICommandSink acs) {
        this.acs = acs;
    }

    private void theActualAIMethod(ConnectFourMessages.ConnectFourGameUpdate.ConnectFourPayload currentState){
        if(this.acs == null){
            logger.error("AI can't submit command, ACS is null");
            return;
        }

        //Fill from left to right
        var targetColumn = -1;
        for(int i=6;i>=0;i--){
            if(currentState.board.get(i).size() < 6){
                targetColumn = i;
            }
        }

        var move = new ConnectFourMessages.ConnectFourMove(targetColumn);
        var wrapped = new CommonMessages.GameAction(move);
        try {
            acs.sendCommand(mapper.writeValueAsString(wrapped));
        }catch(Exception ex){
            logger.error("Unable to send message",ex);
        }
    }
}
