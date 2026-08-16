package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.service.*;

public final class MinecraftServiceImpl implements MinecraftService {

    private final WorldService worldService = new WorldServiceImpl();
    private final PlayerService playerService = new PlayerServiceImpl();
    private final ChatService chatService = new ChatServiceImpl();
    private final NetworkService networkService = new NetworkServiceImpl();
    private final MultiplayerService multiplayerService = new MultiplayerServiceImpl();

    @Override @NotNull public WorldService getWorld() { return worldService; }
    @Override @NotNull public PlayerService getPlayer() { return playerService; }
    @Override @NotNull public ChatService getChat() { return chatService; }
    @Override @NotNull public NetworkService getNetwork() { return networkService; }
    @Override @NotNull public MultiplayerService getMultiplayer() { return multiplayerService; }

    @Override
    @NotNull public String getVersion() {
        return MinecraftClient.getInstance().getGameVersion();
    }

    @Override
    @Nullable public String getCurrentScreen() {
        var screen = MinecraftClient.getInstance().currentScreen;
        return screen != null ? screen.getClass().getSimpleName() : null;
    }

    @Override
    public int getFps() {
        return MinecraftClient.getInstance().getCurrentFps();
    }

    public void unregisterScriptListeners(@NotNull final String scriptName) {
        ((ChatServiceImpl) this.chatService).unregisterListeners(scriptName);
        ((NetworkServiceImpl) this.networkService).unregisterListeners(scriptName);
    }

    public void clearAllScriptListeners() {
        ((ChatServiceImpl) this.chatService).clearAllListeners();
        ((NetworkServiceImpl) this.networkService).clearAllListeners();
    }
}