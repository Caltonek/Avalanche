package pl.caltonek.avalanche.execution.event;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import pl.caltonek.avalanche.execution.script.ScriptContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
    private final Map<String, Map<String, List<LuaValue>>> listeners = new ConcurrentHashMap<>();

    public void subscribe(@NotNull String eventPath, @NotNull LuaValue callback) {
        if (!callback.isfunction()) return;
        String scriptName = ScriptContext.getCurrentScript();
        if (scriptName == null) return;

        listeners.computeIfAbsent(eventPath.toLowerCase(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(scriptName, k -> new CopyOnWriteArrayList<>())
                .add(callback);
    }

    public void unsubscribe(@NotNull String eventPath, @NotNull LuaValue callback) {
        Map<String, List<LuaValue>> scriptListeners = listeners.get(eventPath.toLowerCase());
        if (scriptListeners == null) return;

        for (List<LuaValue> callbackList : scriptListeners.values()) {
            callbackList.remove(callback);
        }
    }

    public void post(@NotNull String eventPath, Object eventData) {
        Map<String, List<LuaValue>> scriptListeners = listeners.get(eventPath.toLowerCase());
        if (scriptListeners == null || scriptListeners.isEmpty()) return;

        LuaValue luaArg = CoerceJavaToLua.coerce(eventData);
        for (List<LuaValue> callbackList : scriptListeners.values()) {
            for (LuaValue callback : callbackList) {
                try {
                    callback.call(luaArg);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void unregisterScript(@NotNull String scriptName) {
        listeners.values().forEach(map -> map.remove(scriptName));
    }

    public void clearAll() {
        listeners.clear();
    }
}