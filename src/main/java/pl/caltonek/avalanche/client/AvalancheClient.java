package pl.caltonek.avalanche.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.service.impl.ChatServiceImpl;
import pl.caltonek.avalanche.api.service.impl.CommandServiceImpl;
import pl.caltonek.avalanche.api.service.impl.MinecraftServiceImpl;
import pl.caltonek.avalanche.command.AvalancheCommand;
import pl.caltonek.avalanche.config.ConfigManager;
import pl.caltonek.avalanche.execution.ScriptExecutionManager;
import pl.caltonek.avalanche.execution.event.EventManager;
import pl.caltonek.avalanche.execution.loader.ScriptLoader;
import pl.caltonek.avalanche.path.AvalanchePaths;

@Environment(EnvType.CLIENT)
public final class AvalancheClient implements ClientModInitializer {

    private static ScriptExecutionManager executionManager;
    private static MinecraftServiceImpl minecraftService;
    private static EventManager eventManager;
    private static ConfigManager configManager;

    @Override
    public void onInitializeClient() {
        AvalanchePaths.createDirectories();

        configManager = new ConfigManager();
        configManager.loadAll();

        eventManager = new EventManager();
        minecraftService = new MinecraftServiceImpl();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ((CommandServiceImpl) minecraftService.getCommand()).setDispatcher(dispatcher);
        });

        final ScriptLoader scriptLoader = new ScriptLoader();

        executionManager = new ScriptExecutionManager(scriptLoader);
        AvalancheCommand.register(executionManager);
    }

    @NotNull
    public static EventManager getEventManager() { return eventManager; }

    @NotNull
    public static ScriptExecutionManager getExecutionManager() { return executionManager; }

    @NotNull
    public static MinecraftServiceImpl getMinecraftService() { return minecraftService; }

    @NotNull
    public static ConfigManager getConfig() {
        return configManager;
    }

    @NotNull
    public static ChatServiceImpl getChatService() {
        return (ChatServiceImpl) minecraftService.getChat();
    }
}