package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

public interface ChatService {
    void send(@NotNull final String message);
    void sendCommand(@NotNull final String command);
    void onChatSend(@NotNull final LuaValue callback);
    void onChatReceive(@NotNull final LuaValue callback);
}