package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;
import pl.caltonek.avalanche.api.signal.LuaSignal;

import java.util.List;
import java.util.UUID;

public interface PlayersService {

    boolean isConnected();
    boolean isSingleplayer();
    @Nullable String getServerAddress();
    @Nullable String getServerBrand();

    @Nullable PlayerObject getLocalPlayer();
    @NotNull List<PlayerObject> getPlayers();
    @Nullable PlayerObject getPlayerByName(@NotNull final String name);
    @Nullable PlayerObject getPlayerByUuid(@NotNull final UUID uuid);

    int getPlayerCount();
    int getLatency();
    int getLatency(@NotNull final String name);

    void disconnect();
    void disconnect(@NotNull final String reason);

    @NotNull LuaSignal getPlayerAdded();
    @NotNull LuaSignal getPlayerRemoving();
    @NotNull LuaSignal getLocalPlayerSpawned();
    @NotNull LuaSignal getLocalPlayerDied();
}