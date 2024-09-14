package de.aiarena.demo.ai;

public interface AI {
    @FunctionalInterface
    public static interface AICommandSink{
        void sendCommand(String command);
    }

    String getName();
    void handleMessage(String message);
    void provideSink(AICommandSink acs);
}
