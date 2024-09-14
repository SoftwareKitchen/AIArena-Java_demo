package de.aiarena.demo.messages;

import java.util.List;

public class CommonMessages {
    public static class AuthenticateMessage{

        public final String action = "identify";
        public final String token;

        public AuthenticateMessage(String token){
            this.token = token;
        }
    }

    public static class JoinMessage{
        public final String match;
        public final String action = "join";

        public JoinMessage(String match){
            this.match = match;
        }
    }

    public static class MatchListMessage{
        public final String action = "match-list";
        public final String game;

        public MatchListMessage(String game){
            this.game = game;
        }
    }

    public static class AIArenaMatchIdentifier{
        public String match;
    }

    public static class OpenLobbiesMessage{
        public List<AIArenaMatchIdentifier> matches;
    }

    public static class GenericInboundMessage{
        public String type;
    }

    public static class GameAction{
        public final String action = "action";
        public final Object payload;

        public GameAction(Object payload){
            this.payload = payload;
        }
    }
}
