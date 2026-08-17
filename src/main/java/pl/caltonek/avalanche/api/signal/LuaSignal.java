package pl.caltonek.avalanche.api.signal;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.client.AvalancheClient;

public final class LuaSignal {

    private final String eventName;

    public LuaSignal(@NotNull final String eventName) {
        this.eventName = eventName;
    }

    @NotNull
    public LuaConnection Connect(@NotNull final LuaValue callback) {
        AvalancheClient.getEventManager().subscribe(this.eventName, callback);
        return new LuaConnection(this.eventName, callback);
    }

    @NotNull
    public LuaConnection connect(@NotNull final LuaValue callback) {
        return Connect(callback);
    }

    public String getEventName() {
        return this.eventName;
    }
}