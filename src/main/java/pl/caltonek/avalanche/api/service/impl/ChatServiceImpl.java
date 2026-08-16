package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.api.service.ChatService;
import pl.caltonek.avalanche.exceptions.ChatServiceException;
import pl.caltonek.avalanche.execution.script.ScriptContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ChatServiceImpl implements ChatService {

    private final Map<String, List<LuaValue>> chatSendListeners = new ConcurrentHashMap<>();
    private final Map<String, List<LuaValue>> chatReceiveListeners = new ConcurrentHashMap<>();

    @Override
    public void send(@NotNull final String message) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null || player.networkHandler == null) {
            throw new ChatServiceException("Cannot send message: player network handler is unavailable.");
        }
        player.networkHandler.sendChatMessage(message);
    }

    @Override
    public void sendCommand(@NotNull final String command) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null || player.networkHandler == null) {
            throw new ChatServiceException("Cannot send command: player network handler is unavailable.");
        }
        final String formattedCommand = command.startsWith("/") ? command.substring(1) : command;
        player.networkHandler.sendCommand(formattedCommand);
    }

    @Override
    public void onChatSend(@NotNull final LuaValue callback) {
        if (!callback.isfunction()) {
            throw new ChatServiceException("Callback for onChatSend must be a function.");
        }
        final String scriptName = ScriptContext.getCurrentScript();
        if (scriptName != null) {
            this.chatSendListeners.computeIfAbsent(scriptName, k -> new CopyOnWriteArrayList<>()).add(callback);
        }
    }

    @Override
    public void onChatReceive(@NotNull final LuaValue callback) {
        if (!callback.isfunction()) {
            throw new ChatServiceException("Callback for onChatReceive must be a function.");
        }
        final String scriptName = ScriptContext.getCurrentScript();
        if (scriptName != null) {
            this.chatReceiveListeners.computeIfAbsent(scriptName, k -> new CopyOnWriteArrayList<>()).add(callback);
        }
    }

    public void unregisterListeners(@NotNull final String scriptName) {
        this.chatSendListeners.remove(scriptName);
        this.chatReceiveListeners.remove(scriptName);
    }

    public void clearAllListeners() {
        this.chatSendListeners.clear();
        this.chatReceiveListeners.clear();
    }

    @NotNull
    public List<LuaValue> getChatSendListeners() {
        final List<LuaValue> allListeners = new ArrayList<>();
        this.chatSendListeners.values().forEach(allListeners::addAll);
        return allListeners;
    }

    @NotNull
    public List<LuaValue> getChatReceiveListeners() {
        final List<LuaValue> allListeners = new ArrayList<>();
        this.chatReceiveListeners.values().forEach(allListeners::addAll);
        return allListeners;
    }
}