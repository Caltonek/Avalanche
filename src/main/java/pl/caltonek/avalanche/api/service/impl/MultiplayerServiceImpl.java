package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.api.object.PlayerObject;
import pl.caltonek.avalanche.api.service.MultiplayerService;
import pl.caltonek.avalanche.client.AvalancheClient;
import pl.caltonek.avalanche.exceptions.MultiplayerServiceException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class MultiplayerServiceImpl implements MultiplayerService {

    @Override
    public boolean isConnected() {
        final var client = MinecraftClient.getInstance();
        return client.getNetworkHandler() != null;
    }

    @Override
    public boolean isSingleplayer() {
        final var client = MinecraftClient.getInstance();
        return client.isIntegratedServerRunning();
    }

    @Override
    @Nullable
    public String getServerAddress() {
        final var client = MinecraftClient.getInstance();
        if (client.getCurrentServerEntry() != null) {
            return client.getCurrentServerEntry().address;
        }
        if (client.isIntegratedServerRunning()) {
            return "singleplayer";
        }
        return null;
    }

    @Override
    @Nullable
    public String getServerBrand() {
        final var client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) {
            return null;
        }
        return client.getNetworkHandler().getBrand();
    }

    @Override
    @NotNull
    public List<PlayerObject> getPlayers() {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();

        if (networkHandler == null) {
            return Collections.emptyList();
        }

        return networkHandler.getPlayerList()
                .stream()
                .map(this::mapToPlayerObject)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public PlayerObject getPlayer(@NotNull final String name) {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();

        if (networkHandler == null) {
            return null;
        }

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

        if (networkHandler == null) {
            return null;
        }

        return networkHandler.getPlayerList()
                .stream()
                .filter(entry -> entry.getProfile().getId().equals(uuid))
                .findFirst()
                .map(this::mapToPlayerObject)
                .orElse(null);
    }

    @Override
    public int getPlayerCount() {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();

        return networkHandler != null ? networkHandler.getPlayerList().size() : 0;
    }

    @Override
    public int getLatency() {
        final var client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) {
            return -1;
        }
        final PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : -1;
    }

    @Override
    public int getLatency(@NotNull final String name) {
        final var player = getPlayer(name);
        return player != null ? player.getLatency() : -1;
    }

    @Override
    public void disconnect() {
        disconnect("Disconnected via script");
    }

    @Override
    public void disconnect(@NotNull final String reason) {
        final var client = MinecraftClient.getInstance();
        if (!isConnected()) {
            throw new MultiplayerServiceException("Cannot disconnect: Client is not connected to any server.");
        }

        if (client.world != null) {
            client.world.disconnect();
        }
        client.disconnect(new MultiplayerScreen(new TitleScreen()));
    }

    // Players
    @Override public void onPlayerJoin(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerjoin", callback); }
    @Override public void onPlayerLeave(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerleave", callback); }
    @Override public void onPlayerSpawn(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerspawn", callback); }
    @Override public void onPlayerDespawn(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerdespawn", callback); }
    @Override public void onPlayerUpdate(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerupdate", callback); }
    @Override public void onPlayerMove(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayermove", callback); }
    @Override public void onPlayerTeleport(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerteleport", callback); }
    @Override public void onPlayerDeath(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerdeath", callback); }
    @Override public void onPlayerRespawn(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.players.onplayerrespawn", callback); }

    // PlayerState
    @Override public void onHealthChange(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.playerstate.onhealthchange", callback); }
    @Override public void onEquipmentChange(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.playerstate.onequipmentchange", callback); }
    @Override public void onHeldItemChange(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.playerstate.onhelditemchange", callback); }
    @Override public void onGameModeChange(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.playerstate.ongamemodechange", callback); }
    @Override public void onSneakChange(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.playerstate.onsneakchange", callback); }
    @Override public void onSprintChange(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.playerstate.onsprintchange", callback); }

    // List
    @Override public void onPlayerListAdd(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.list.onplayerlistadd", callback); }
    @Override public void onPlayerListRemove(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.list.onplayerlistremove", callback); }
    @Override public void onPlayerListUpdate(@NotNull LuaValue callback) { AvalancheClient.getEventManager().subscribe("multiplayer.list.onplayerlistupdate", callback); }

    @NotNull
    private PlayerObject mapToPlayerObject(@NotNull final PlayerListEntry entry) {
        final var client = MinecraftClient.getInstance();
        final UUID uuid = entry.getProfile().getId();
        final String name = entry.getProfile().getName();

        final var worldPlayer = client.world != null ? client.world.getPlayerByUuid(uuid) : null;

        if (worldPlayer != null) {
            return new PlayerObject(
                    name,
                    uuid,
                    worldPlayer.getX(),
                    worldPlayer.getY(),
                    worldPlayer.getZ(),
                    worldPlayer.getHealth(),
                    worldPlayer.getMaxHealth(),
                    worldPlayer.getHungerManager().getFoodLevel(),
                    worldPlayer.isCreative() ? "creative" : "survival",
                    entry.getLatency(),
                    worldPlayer.getId(),
                    worldPlayer.isSneaking(),
                    worldPlayer.isSprinting(),
                    worldPlayer.isOnGround()
            );
        }

        final String gameMode = entry.getGameMode() != null ? entry.getGameMode().getName() : "unknown";

        return new PlayerObject(
                name,
                uuid,
                0.0,
                0.0,
                0.0,
                20.0f,
                20.0f,
                20,
                gameMode,
                entry.getLatency(),
                -1,
                false,
                false,
                false
        );
    }
}