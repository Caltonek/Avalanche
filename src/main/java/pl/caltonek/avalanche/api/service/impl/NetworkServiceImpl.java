package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
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
        final var handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler == null) {
            throw new NetworkServiceException("Cannot send packet: network handler is unavailable.");
        }
    }

    @Override
    public void sendCustomPayload(@NotNull final String channel, @NotNull final byte[] data) {
        final var handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler == null) {
            throw new NetworkServiceException("Cannot send custom payload: network handler is unavailable.");
        }

        try {
            final Identifier channelId = Identifier.of(channel);
            handler.sendPacket(new CustomPayloadC2SPacket(new CustomPayload() {
                @Override
                public Id<? extends CustomPayload> getId() {
                    return new Id<>(channelId);
                }
            }));
        } catch (Exception e) {
            throw new NetworkServiceException("Failed to send custom payload to channel: " + channel, e);
        }
    }

    @Override
    public boolean isConnected() {
        return MinecraftClient.getInstance().getNetworkHandler() != null;
    }

    @Override
    public int getLatency() {
        final var client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) {
            return -1;
        }
        final var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : -1;
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