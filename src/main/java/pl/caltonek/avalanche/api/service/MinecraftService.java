package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MinecraftService {
    @NotNull WorldService getWorld();
    @NotNull PlayersService getPlayer();
    @NotNull PlayersService getPlayers();
    @NotNull ChatService getChat();
    @NotNull CommandService getCommand();
    @NotNull NetworkService getNetwork();
    @NotNull InventoryService getInventory();
    @NotNull UserInputService getInput();
    @NotNull HttpService getHttp();
    @NotNull RunService getRun();

    @NotNull String getVersion();
    @Nullable String getCurrentScreen();
    int getFps();
}