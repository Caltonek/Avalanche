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
    public String GetCurrentScreen() {
        return this.minecraftService.getCurrentScreen();
    }

    @Nullable
    public String GetVersion() {
        return this.minecraftService.getVersion();
    }

    @Nullable
    public Object GetService(@NotNull final String serviceName) {
        return switch (serviceName.toLowerCase()) {
            case "players", "playerservice", "player" -> this.minecraftService.getPlayers();
            case "userinputservice", "input", "inputservice" -> this.minecraftService.getInput();
            case "httpservice", "http" -> this.minecraftService.getHttp();
            case "runservice", "run" -> this.minecraftService.getRun();
            case "chat", "chatservice" -> this.minecraftService.getChat();
            case "command", "commandservice" -> this.minecraftService.getCommand();
            case "workspace", "world", "worldservice" -> this.minecraftService.getWorld();
            case "network", "networkservice" -> this.minecraftService.getNetwork();
            case "inventory", "inventoryservice" -> this.minecraftService.getInventory();
            case "minecraft", "minecraftservice" -> this.minecraftService;
            default -> null;
        };
    }

    @Nullable
    public Object getService(@NotNull final String serviceName) {
        return GetService(serviceName);
    }
}