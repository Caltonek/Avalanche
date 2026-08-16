package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;
import pl.caltonek.avalanche.api.service.MultiplayerService;

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
    public int getPlayerCount() {
        final var client = MinecraftClient.getInstance();
        final var networkHandler = client.getNetworkHandler();

        return networkHandler != null ? networkHandler.getPlayerList().size() : 0;
    }

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