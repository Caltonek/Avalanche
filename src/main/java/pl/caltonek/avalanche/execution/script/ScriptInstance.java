package pl.caltonek.avalanche.execution.script;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface ScriptInstance {

    @NotNull
    String getName();

    @NotNull
    Path getPath();

    @NotNull
    ScriptState getState();

    void execute();

    void halt();

    boolean isRunning();
}
