package pl.caltonek.avalanche.execution;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.client.AvalancheClient;
import pl.caltonek.avalanche.execution.loader.ScriptLoader;
import pl.caltonek.avalanche.execution.script.ScriptExecutor;
import pl.caltonek.avalanche.execution.script.ScriptState;
import pl.caltonek.avalanche.path.AvalanchePaths;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ScriptExecutionManager {

    private final ScriptLoader scriptLoader;
    private final Map<String, ScriptExecutor> activeScripts;
    private final ExecutorService executorService;

    public ScriptExecutionManager(@NotNull final ScriptLoader scriptLoader) {
        this.scriptLoader = scriptLoader;
        this.activeScripts = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    @NotNull
    public List<String> getAvailableScripts() {
        return this.scriptLoader.findAvailableScripts();
    }

    public boolean execute(@NotNull final String scriptInput) {
        Path scriptPath;
        String scriptName;

        if (this.scriptLoader.isUrl(scriptInput)) {
            try {
                final String content = AvalancheClient.getMinecraftService().getHttp().getAsync(scriptInput);

                final Path cacheDir = AvalanchePaths.SCRIPTS_DIR.resolve("cache");
                if (!Files.exists(cacheDir)) Files.createDirectories(cacheDir);

                scriptName = "remote_" + Integer.toHexString(scriptInput.hashCode()) + ".lua";
                scriptPath = cacheDir.resolve(scriptName);

                Files.writeString(scriptPath, content);
            } catch (final Exception e) {
                throw new RuntimeException("Failed to download script from URL: " + scriptInput, e);
            }
        } else {
            scriptName = this.scriptLoader.normalizeScriptName(scriptInput);
            scriptPath = this.scriptLoader.resolveScriptPath(scriptInput);
        }

        final boolean reloaded = this.activeScripts.containsKey(scriptName);

        if (reloaded) {
            this.halt(scriptName);
        }

        final ScriptExecutor scriptExecutor = new ScriptExecutor(scriptName, scriptPath);
        this.activeScripts.put(scriptName, scriptExecutor);

        final var future = this.executorService.submit(() -> {
            try {
                scriptExecutor.execute();
            } catch (final Throwable throwable) {
                this.halt(scriptName);

                final StringWriter sw = new StringWriter();
                throwable.printStackTrace(new PrintWriter(sw));

                final String cleanStackTrace = sw.toString()
                        .replace("\r", "")
                        .replace("\t", "    ");

                final var client = MinecraftClient.getInstance();
                if (client.player != null) {
                    client.execute(() -> {
                        MutableText errorMsg = Text.literal("[Exec Error] " + throwable.getMessage())
                                .formatted(Formatting.RED)
                                .styled(style -> style.withHoverEvent(
                                        new HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                Text.literal("§c" + cleanStackTrace)
                                        )
                                ));

                        client.player.sendMessage(errorMsg, false);
                    });
                }
                throwable.printStackTrace();
            } finally {
                if (scriptExecutor.getState() == ScriptState.FAILED) {
                    this.activeScripts.remove(scriptName, scriptExecutor);
                }
            }
        });

        scriptExecutor.setExecutionTask(future);
        return reloaded;
    }

    public void halt(@NotNull final String scriptName) {
        final String normalizedName = this.scriptLoader.normalizeScriptName(scriptName);
        final ScriptExecutor scriptExecutor = this.activeScripts.remove(normalizedName);

        if (scriptExecutor != null) {
            scriptExecutor.halt();
        }

        AvalancheClient.getMinecraftService().unregisterScriptListeners(normalizedName);
        AvalancheClient.getEventManager().unregisterScript(normalizedName);
    }

    public void haltAll() {
        this.activeScripts.values().forEach(ScriptExecutor::halt);
        this.activeScripts.clear();
        AvalancheClient.getMinecraftService().clearAllListeners();
        AvalancheClient.getEventManager().clearAll();
    }

    @NotNull
    public Collection<ScriptExecutor> getActiveScripts() {
        return this.activeScripts.values();
    }

    @Nullable
    public ScriptExecutor getActiveScript(@NotNull final String scriptName) {
        return this.activeScripts.get(this.scriptLoader.normalizeScriptName(scriptName));
    }

    public boolean isRunning(@NotNull final String scriptName) {
        final ScriptExecutor script = getActiveScript(scriptName);
        return script != null && script.isRunning();
    }

    @NotNull
    public ScriptLoader getScriptLoader() {
        return this.scriptLoader;
    }

    public void shutdown() {
        haltAll();
        this.executorService.shutdownNow();
    }
}