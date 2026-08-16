package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;

import java.util.List;
import java.util.UUID;

public interface MultiplayerService {

    boolean isConnected();

    boolean isSingleplayer();

    @Nullable String getServerAddress();

    @Nullable String getServerBrand();

    @NotNull List<PlayerObject> getPlayers();

    @Nullable PlayerObject getPlayer(@NotNull final String name);

    @Nullable PlayerObject getPlayerByUuid(@NotNull final UUID uuid);

    int getPlayerCount();

    int getLatency();

    int getLatency(@NotNull final String name);

    void disconnect();

    void disconnect(@NotNull final String reason);
}