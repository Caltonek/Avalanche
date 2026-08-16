package pl.caltonek.avalanche.exceptions;


public class ScriptExecutionException extends RuntimeException {

    public ScriptExecutionException(
            final String message
    ) {
        super(message);
    }

    public ScriptExecutionException(
            final String message,
            final Throwable cause
    ) {
        super(message, cause);
    }
}