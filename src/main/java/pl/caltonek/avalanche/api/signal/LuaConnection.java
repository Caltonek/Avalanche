package pl.caltonek.avalanche.api.signal;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.client.AvalancheClient;

public final class LuaConnection {

    private final String eventName;
    private final LuaValue callback;
    private boolean connected = true;

    public LuaConnection(@NotNull final String eventName, @NotNull final LuaValue callback) {
        this.eventName = eventName;
        this.callback = callback;
    }

    public void Disconnect() {
        if (connected) {
            AvalancheClient.getEventManager().unsubscribe(this.eventName, this.callback);
            this.connected = false;
        }
    }

    public void disconnect() {
        Disconnect();
    }

    public boolean isConnected() {
        return this.connected;
    }
}