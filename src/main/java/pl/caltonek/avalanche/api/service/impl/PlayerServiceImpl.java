package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.PlayerObject;
import pl.caltonek.avalanche.api.service.PlayerService;

public final class PlayerServiceImpl implements PlayerService {

    @Override
    @Nullable public PlayerObject getLocal() {
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
}