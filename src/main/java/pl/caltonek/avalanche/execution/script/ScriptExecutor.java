package pl.caltonek.avalanche.execution.script;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import pl.caltonek.avalanche.api.Game;
import pl.caltonek.avalanche.api.service.MinecraftService;
import pl.caltonek.avalanche.client.AvalancheClient;
import pl.caltonek.avalanche.exceptions.ScriptExecutionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;

public final class ScriptExecutor implements ScriptInstance {

    private final String name;
    private final Path path;
    private final MinecraftService minecraftService;

    private volatile ScriptState state = ScriptState.HALTED;
    private volatile Future<?> executionTask;

    public ScriptExecutor(@NotNull final String name, @NotNull final Path path) {
        this.name = name;
        this.path = path;
        this.minecraftService = AvalancheClient.getMinecraftService();
    }

    @Override @NotNull public String getName() { return name; }
    @Override @NotNull public Path getPath() { return path; }
    @Override @NotNull public ScriptState getState() { return state; }

    public void setExecutionTask(@NotNull final Future<?> executionTask) {
        this.executionTask = executionTask;
    }

    @Override
    public void execute() {
        if (state == ScriptState.RUNNING) return;
        state = ScriptState.RUNNING;

        try {
            ScriptContext.setCurrentScript(name);
            final String source = Files.readString(path);
            final Globals globals = JsePlatform.standardGlobals();

            globals.set("luajava", LuaValue.NIL);
            globals.set("dofile", LuaValue.NIL);
            globals.set("loadfile", LuaValue.NIL);

            globals.set("game", CoerceJavaToLua.coerce(new Game(minecraftService)));

            globals.set("minecraft", CoerceJavaToLua.coerce(minecraftService));

            final LuaValue script = globals.load(source, name);
            script.call();
        } catch (final IOException exception) {
            state = ScriptState.FAILED;
            throw new ScriptExecutionException("Unable to read script: " + name, exception);
        } catch (final RuntimeException exception) {
            state = ScriptState.FAILED;
            throw new ScriptExecutionException("Failed to execute script: " + name, exception);
        } finally {
            ScriptContext.clear();
        }
    }

    @Override
    public void halt() {
        if (executionTask != null) {
            executionTask.cancel(true);
        }
        state = ScriptState.HALTED;
    }

    @Override
    public boolean isRunning() {
        return state == ScriptState.RUNNING;
    }
}