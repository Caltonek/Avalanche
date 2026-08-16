package pl.caltonek.avalanche.execution.script;

import org.jetbrains.annotations.NotNull;

public enum ScriptState {
    RUNNING,
    HALTED,
    FAILED;

    @NotNull
    public String getDisplayName() {
        return name().toLowerCase();
    }
}
