package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaValue;
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

    // Players
    void onPlayerJoin(@NotNull LuaValue callback);
    void onPlayerLeave(@NotNull LuaValue callback);
    void onPlayerSpawn(@NotNull LuaValue callback);
    void onPlayerDespawn(@NotNull LuaValue callback);
    void onPlayerUpdate(@NotNull LuaValue callback);
    void onPlayerMove(@NotNull LuaValue callback);
    void onPlayerTeleport(@NotNull LuaValue callback);
    void onPlayerDeath(@NotNull LuaValue callback);
    void onPlayerRespawn(@NotNull LuaValue callback);

    // PlayerState
    void onHealthChange(@NotNull LuaValue callback);
    void onEquipmentChange(@NotNull LuaValue callback);
    void onHeldItemChange(@NotNull LuaValue callback);
    void onGameModeChange(@NotNull LuaValue callback);
    void onSneakChange(@NotNull LuaValue callback);
    void onSprintChange(@NotNull LuaValue callback);

    // List
    void onPlayerListAdd(@NotNull LuaValue callback);
    void onPlayerListRemove(@NotNull LuaValue callback);
    void onPlayerListUpdate(@NotNull LuaValue callback);
}