package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public interface ChatService {
    void send(@NotNull final String message);
    void sendCommand(@NotNull final String command);
    void print(@NotNull final String message);

    @NotNull LuaSignal getMessageReceived();
    @NotNull LuaSignal getMessageSent();
    @NotNull LuaSignal getSystemMessage();
    @NotNull LuaSignal getActionBar();
}