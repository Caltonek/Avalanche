package pl.caltonek.avalanche.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.service.impl.ChatServiceImpl;
import pl.caltonek.avalanche.api.service.impl.MinecraftServiceImpl;
import pl.caltonek.avalanche.command.AvalancheCommand;
import pl.caltonek.avalanche.execution.ScriptExecutionManager;
import pl.caltonek.avalanche.execution.loader.ScriptLoader;

import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public final class AvalancheClient implements ClientModInitializer {

    private static ScriptExecutionManager executionManager;
    private static MinecraftServiceImpl minecraftService;

    @Override
    public void onInitializeClient() {
        minecraftService = new MinecraftServiceImpl();

        final Path scriptsDirectory = Path.of("avalanche", "scripts");
        final ScriptLoader scriptLoader = new ScriptLoader(scriptsDirectory);

        executionManager = new ScriptExecutionManager(scriptLoader);
        AvalancheCommand.register(executionManager);
    }

    @NotNull
    public static ScriptExecutionManager getExecutionManager() { return executionManager; }

    @NotNull
    public static MinecraftServiceImpl getMinecraftService() { return minecraftService; }

    @NotNull
    public static ChatServiceImpl getChatService() {
        return (ChatServiceImpl) minecraftService.getChat();
    }
}