package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.service.ChatService;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public final class ChatServiceImpl implements ChatService {

    public final LuaSignal MessageReceived = new LuaSignal("chat.incoming.onchatreceive");
    public final LuaSignal MessageSent = new LuaSignal("chat.outgoing.onchatsend");
    public final LuaSignal SystemMessage = new LuaSignal("chat.incoming.onsystemmessage");
    public final LuaSignal ActionBar = new LuaSignal("chat.incoming.onactionbar");

    @Override
    public void send(@NotNull final String message) {
        Send(message);
    }

    public void Send(@NotNull final String message) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null || player.networkHandler == null) return;

        if (message.startsWith("/")) {
            SendCommand(message);
            return;
        }
        player.networkHandler.sendChatMessage(message);
    }

    @Override
    public void sendCommand(@NotNull final String command) {
        SendCommand(command);
    }

    public void SendCommand(@NotNull final String command) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null || player.networkHandler == null) return;
        final String formatted = command.startsWith("/") ? command.substring(1) : command;
        player.networkHandler.sendCommand(formatted);
    }

    @Override
    public void print(@NotNull final String message) {
        Print(message);
    }

    public void Print(@NotNull final String message) {
        final var client = MinecraftClient.getInstance();
        if (client.inGameHud != null) {
            client.inGameHud.getChatHud().addMessage(Text.literal(message));
        }
    }

    @Override @NotNull public LuaSignal getMessageReceived() { return MessageReceived; }
    @Override @NotNull public LuaSignal getMessageSent() { return MessageSent; }
    @Override @NotNull public LuaSignal getSystemMessage() { return SystemMessage; }
    @Override @NotNull public LuaSignal getActionBar() { return ActionBar; }
}