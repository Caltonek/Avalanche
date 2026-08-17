package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.service.*;

public final class MinecraftServiceImpl implements MinecraftService {

    private final WorldServiceImpl worldService = new WorldServiceImpl();
    private final PlayersServiceImpl playersService = new PlayersServiceImpl();
    private final ChatServiceImpl chatService = new ChatServiceImpl();
    private final CommandServiceImpl commandService = new CommandServiceImpl();
    private final NetworkServiceImpl networkService = new NetworkServiceImpl();
    private final InventoryServiceImpl inventoryService = new InventoryServiceImpl();
    private final UserInputServiceImpl inputService = new UserInputServiceImpl();
    private final HttpServiceImpl httpService = new HttpServiceImpl();
    private final RunServiceImpl runService = new RunServiceImpl();

    @Override @NotNull public WorldService getWorld() { return worldService; }
    @Override @NotNull public PlayersService getPlayer() { return playersService; }
    @Override @NotNull public PlayersService getPlayers() { return playersService; }
    @Override @NotNull public ChatService getChat() { return chatService; }
    @Override @NotNull public CommandService getCommand() { return commandService; }
    @Override @NotNull public NetworkService getNetwork() { return networkService; }
    @Override @NotNull public InventoryService getInventory() { return inventoryService; }
    @Override @NotNull public UserInputService getInput() { return inputService; }
    @Override @NotNull public HttpService getHttp() { return httpService; }
    @Override @NotNull public RunService getRun() { return runService; }

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

    public void unregisterScriptListeners(@NotNull String scriptName) {
        this.commandService.unregisterListeners(scriptName);
        this.networkService.unregisterListeners(scriptName);
    }

    public void clearAllListeners() {
        this.commandService.clearAllListeners();
        this.networkService.clearAllListeners();
    }

    public void clearAllScriptListeners() {
        clearAllListeners();
    }
}