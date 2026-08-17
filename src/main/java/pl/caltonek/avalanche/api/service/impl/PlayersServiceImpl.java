package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;
import pl.caltonek.avalanche.api.service.PlayersService;
import pl.caltonek.avalanche.api.signal.LuaSignal;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PlayersServiceImpl implements PlayersService {

    public final LuaSignal PlayerAdded = new LuaSignal("multiplayer.players.onplayerjoin");
    public final LuaSignal PlayerRemoving = new LuaSignal("multiplayer.players.onplayerleave");
    public final LuaSignal LocalPlayerSpawned = new LuaSignal("player.lifecycle.onspawn");
    public final LuaSignal LocalPlayerDied = new LuaSignal("player.lifecycle.ondeath");

    @Override
    public boolean isConnected() {
        return MinecraftClient.getInstance().getNetworkHandler() != null;
    }

    @Override
    public boolean isSingleplayer() {
        return MinecraftClient.getInstance().isInSingleplayer();
    }

    @Override
    @Nullable
    public String getServerAddress() {
        var entry = MinecraftClient.getInstance().getCurrentServerEntry();
        return entry != null ? entry.address : null;
    }

    @Override
    @Nullable
    public String getServerBrand() {
        var player = MinecraftClient.getInstance().player;
        return (player != null && player.networkHandler != null) ? player.networkHandler.getBrand() : null;
    }

    @Override
    @Nullable
    public PlayerObject getLocalPlayer() {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        if (player == null) return null;

        var entry = client.getNetworkHandler() != null ? client.getNetworkHandler().getPlayerListEntry(player.getUuid()) : null;

        return new PlayerObject(
                player.getName().getString(),
                player.getUuid(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getHealth(),
                player.getMaxHealth(),
                player.getHungerManager().getFoodLevel(),
                player.isCreative() ? "creative" : "survival",
                entry != null ? entry.getLatency() : 0,
                player.getId(),
                player.isSneaking(),
                player.isSprinting(),
                player.isOnGround()
        );
    }

    @Override
    @NotNull
    public List<PlayerObject> getPlayers() {
        return GetPlayers();
    }

    @NotNull
    public List<PlayerObject> GetPlayers() {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();
        if (networkHandler == null) return Collections.emptyList();

        return networkHandler.getPlayerList()
                .stream()
                .map(this::mapToPlayerObject)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public PlayerObject getPlayerByName(@NotNull final String name) {
        return GetPlayerByName(name);
    }

    @Nullable
    public PlayerObject GetPlayerByName(@NotNull final String name) {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();
        if (networkHandler == null) return null;

        return networkHandler.getPlayerList()
                .stream()
                .filter(entry -> entry.getProfile().getName().equalsIgnoreCase(name))
                .findFirst()
                .map(this::mapToPlayerObject)
                .orElse(null);
    }

    @Override
    @Nullable
    public PlayerObject getPlayerByUuid(@NotNull final UUID uuid) {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();
        if (networkHandler == null) return null;

        return networkHandler.getPlayerList()
                .stream()
                .filter(entry -> entry.getProfile().getId().equals(uuid))
                .findFirst()
                .map(this::mapToPlayerObject)
                .orElse(null);
    }

    @Override
    public int getPlayerCount() {
        var nh = MinecraftClient.getInstance().getNetworkHandler();
        return nh != null ? nh.getPlayerList().size() : 0;
    }

    @Override
    public int getLatency() {
        var local = getLocalPlayer();
        return local != null ? local.getLatency() : -1;
    }

    @Override
    public int getLatency(@NotNull final String name) {
        var player = getPlayerByName(name);
        return player != null ? player.getLatency() : -1;
    }

    @Override
    public void disconnect() {
        disconnect("Disconnected by script");
    }

    @Override
    public void disconnect(@NotNull final String reason) {
        var nh = MinecraftClient.getInstance().getNetworkHandler();
        if (nh != null) {
            nh.getConnection().disconnect(Text.literal(reason));
        }
    }

    @Override @NotNull public LuaSignal getPlayerAdded() { return PlayerAdded; }
    @Override @NotNull public LuaSignal getPlayerRemoving() { return PlayerRemoving; }
    @Override @NotNull public LuaSignal getLocalPlayerSpawned() { return LocalPlayerSpawned; }
    @Override @NotNull public LuaSignal getLocalPlayerDied() { return LocalPlayerDied; }

    @NotNull
    private PlayerObject mapToPlayerObject(@NotNull final PlayerListEntry entry) {
        final var client = MinecraftClient.getInstance();
        final UUID uuid = entry.getProfile().getId();
        final String name = entry.getProfile().getName();
        final var worldPlayer = client.world != null ? client.world.getPlayerByUuid(uuid) : null;

        if (worldPlayer != null) {
            return new PlayerObject(
                    name, uuid, worldPlayer.getX(), worldPlayer.getY(), worldPlayer.getZ(),
                    worldPlayer.getHealth(), worldPlayer.getMaxHealth(),
                    worldPlayer.getHungerManager().getFoodLevel(),
                    worldPlayer.isCreative() ? "creative" : "survival",
                    entry.getLatency(), worldPlayer.getId(),
                    worldPlayer.isSneaking(), worldPlayer.isSprinting(), worldPlayer.isOnGround()
            );
        }

        return new PlayerObject(
                name, uuid, 0.0, 0.0, 0.0, 20.0f, 20.0f, 20,
                entry.getGameMode() != null ? entry.getGameMode().getName() : "unknown",
                entry.getLatency(), -1, false, false, false
        );
    }
}