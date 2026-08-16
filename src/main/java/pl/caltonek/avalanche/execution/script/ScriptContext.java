package pl.caltonek.avalanche.execution.script;

public final class ScriptContext {

    private static final ThreadLocal<String> CURRENT_SCRIPT = new ThreadLocal<>();

    public static void setCurrentScript(final String scriptName) {
        CURRENT_SCRIPT.set(scriptName);
    }

    public static String getCurrentScript() {
        return CURRENT_SCRIPT.get();
    }

    public static void clear() {
        CURRENT_SCRIPT.remove();
    }
}