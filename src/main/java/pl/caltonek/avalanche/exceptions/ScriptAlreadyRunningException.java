package pl.caltonek.avalanche.exceptions;

import org.jetbrains.annotations.NotNull;

public final class ScriptAlreadyRunningException extends ScriptExecutionException {

    public ScriptAlreadyRunningException(
            @NotNull final String scriptName
    ) {
        super("Script is already running: " + scriptName);
    }
}
