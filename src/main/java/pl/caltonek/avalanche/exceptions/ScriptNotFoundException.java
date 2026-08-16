package pl.caltonek.avalanche.exceptions;

import org.jetbrains.annotations.NotNull;

public final class ScriptNotFoundException extends ScriptExecutionException {

    public ScriptNotFoundException(
            @NotNull final String scriptName
    ) {
        super("Script not found: " + scriptName);
    }
}
