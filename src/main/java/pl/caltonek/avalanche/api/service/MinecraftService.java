package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MinecraftService {
    @NotNull WorldService getWorld();
    @NotNull PlayerService getPlayer();
    @NotNull ChatService getChat();
    @NotNull NetworkService getNetwork();
    @NotNull MultiplayerService getMultiplayer();
    @NotNull String getVersion();
    @Nullable String getCurrentScreen();
    int getFps();
}