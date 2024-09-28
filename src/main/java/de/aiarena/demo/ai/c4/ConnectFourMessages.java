package de.aiarena.demo.ai.c4;

import de.aiarena.demo.messages.CommonMessages;

import java.util.List;

public class ConnectFourMessages {
    public static class ConnectFourGameUpdate{
        public static class ConnectFourPayload{
            public List<List<Integer>> board;
        }
        public ConnectFourPayload state;
        public Integer slot;
        public Integer activePlayer;
        public CommonMessages.AIArenaMatchIdentifier gi;
    }

    public static class ConnectFourMove{
        public final int col;
        public ConnectFourMove(int col){
            this.col = col;
        }
    }
}
