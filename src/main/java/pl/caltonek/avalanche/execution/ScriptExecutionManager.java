package pl.caltonek.avalanche.execution;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.client.AvalancheClient;
import pl.caltonek.avalanche.exceptions.ScriptAlreadyRunningException;
import pl.caltonek.avalanche.execution.loader.ScriptLoader;
import pl.caltonek.avalanche.execution.script.ScriptExecutor;

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

    public void execute(@NotNull final String scriptName) {
        final String normalizedName = this.scriptLoader.normalizeScriptName(scriptName);
        final Path scriptPath = this.scriptLoader.resolveScriptPath(scriptName);

        final ScriptExecutor existingScript = this.activeScripts.get(normalizedName);
        if (existingScript != null && existingScript.isRunning()) {
            throw new ScriptAlreadyRunningException(normalizedName);
        }

        final ScriptExecutor scriptExecutor = new ScriptExecutor(normalizedName, scriptPath);
        this.activeScripts.put(normalizedName, scriptExecutor);

        final var future = this.executorService.submit(() -> {
            try {
                scriptExecutor.execute();
            } finally {
                if (!scriptExecutor.isRunning()) {
                    this.activeScripts.remove(normalizedName, scriptExecutor);
                }
            }
        });

        scriptExecutor.setExecutionTask(future);
    }

    public void halt(@NotNull final String scriptName) {
        final String normalizedName = this.scriptLoader.normalizeScriptName(scriptName);
        final ScriptExecutor scriptExecutor = this.activeScripts.remove(normalizedName);

        if (scriptExecutor != null) {
            scriptExecutor.halt();
        }
        AvalancheClient.getChatService().unregisterListeners(normalizedName);
    }

    public void haltAll() {
        this.activeScripts.values().forEach(ScriptExecutor::halt);
        this.activeScripts.clear();
        AvalancheClient.getChatService().clearAllListeners();
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