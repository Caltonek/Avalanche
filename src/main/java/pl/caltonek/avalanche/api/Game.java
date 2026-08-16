package pl.caltonek.avalanche.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.service.MinecraftService;

public final class Game {

    private final MinecraftService minecraftService;

    public Game(@NotNull final MinecraftService minecraftService) {
        this.minecraftService = minecraftService;
    }

    @Nullable
    public Object GetService(@NotNull final String serviceName) {
        return switch (serviceName.toLowerCase()) {
            case "chat" -> this.minecraftService.getChat();
            case "player" -> this.minecraftService.getPlayer();
            case "world" -> this.minecraftService.getWorld();
            case "network" -> this.minecraftService.getNetwork();
            case "multiplayer" -> this.minecraftService.getMultiplayer();
            case "minecraft" -> this.minecraftService;
            default -> null;
        };
    }
}