package de.aiarena.demo.ai.c4;

import java.util.List;

public class ConnectFourMessages {
    public static class ConnectFourGameUpdate{
        public static class ConnectFourPayload{
            public List<List<Integer>> board;
        }
        public ConnectFourPayload state;
        public Integer slot;
        public Integer activePlayer;
    }

    public static class ConnectFourMove{
        public final int col;
        public ConnectFourMove(int col){
            this.col = col;
        }
    }
}
