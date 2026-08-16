package pl.caltonek.avalanche.api.service.impl;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.api.object.PacketObject;
import pl.caltonek.avalanche.api.service.NetworkService;
import pl.caltonek.avalanche.exceptions.NetworkServiceException;
import pl.caltonek.avalanche.execution.script.ScriptContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NetworkServiceImpl implements NetworkService {

    private final Map<String, List<LuaValue>> sendPacketListeners = new ConcurrentHashMap<>();
    private final Map<String, List<LuaValue>> receivePacketListeners = new ConcurrentHashMap<>();

    @Override
    public void sendPacket(@NotNull final PacketObject packet) {
        if (packet.getType().isEmpty()) {
            throw new NetworkServiceException("Cannot send empty packet type.");
        }
    }

    @Override
    public void onSendPacket(@NotNull final LuaValue callback) {
        if (!callback.isfunction()) {
            throw new NetworkServiceException("Callback for onSendPacket must be a function.");
        }
        final String scriptName = ScriptContext.getCurrentScript();
        if (scriptName != null) {
            this.sendPacketListeners.computeIfAbsent(scriptName, k -> new CopyOnWriteArrayList<>()).add(callback);
        }
    }

    @Override
    public void onReceivePacket(@NotNull final LuaValue callback) {
        if (!callback.isfunction()) {
            throw new NetworkServiceException("Callback for onReceivePacket must be a function.");
        }
        final String scriptName = ScriptContext.getCurrentScript();
        if (scriptName != null) {
            this.receivePacketListeners.computeIfAbsent(scriptName, k -> new CopyOnWriteArrayList<>()).add(callback);
        }
    }

    public void unregisterListeners(@NotNull final String scriptName) {
        this.sendPacketListeners.remove(scriptName);
        this.receivePacketListeners.remove(scriptName);
    }

    public void clearAllListeners() {
        this.sendPacketListeners.clear();
        this.receivePacketListeners.clear();
    }

    @NotNull
    public List<LuaValue> getSendPacketListeners() {
        final List<LuaValue> allListeners = new ArrayList<>();
        this.sendPacketListeners.values().forEach(allListeners::addAll);
        return allListeners;
    }

    @NotNull
    public List<LuaValue> getReceivePacketListeners() {
        final List<LuaValue> allListeners = new ArrayList<>();
        this.receivePacketListeners.values().forEach(allListeners::addAll);
        return allListeners;
    }
}