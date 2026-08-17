package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.object.PacketObject;
import pl.caltonek.avalanche.api.service.NetworkService;
import pl.caltonek.avalanche.api.signal.LuaSignal;
import pl.caltonek.avalanche.exceptions.NetworkServiceException;

public final class NetworkServiceImpl implements NetworkService {

    public final LuaSignal PacketSend = new LuaSignal("network.packets.onpacketsend");
    public final LuaSignal PacketReceive = new LuaSignal("network.packets.onpacketreceive");
    public final LuaSignal ConnectedSignal = new LuaSignal("network.connection.onconnect");
    public final LuaSignal DisconnectedSignal = new LuaSignal("network.connection.ondisconnect");

    @Override
    public void sendPacket(@NotNull final PacketObject packet) {
        if (packet.getType().isEmpty()) {
            throw new NetworkServiceException("Cannot send empty packet type.");
        }
        final var handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler == null) {
            throw new NetworkServiceException("Cannot send packet: network handler is unavailable.");
        }

        if (packet.getRawPacket() instanceof Packet<?> mcPacket) {
            handler.sendPacket(mcPacket);
        } else {
            throw new NetworkServiceException("Unsupported packet object format.");
        }
    }

    public void SendPacket(@NotNull final PacketObject packet) {
        sendPacket(packet);
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

    public void SendCustomPayload(@NotNull final String channel, @NotNull final byte[] data) {
        sendCustomPayload(channel, data);
    }

    @Override
    public boolean isConnected() {
        return MinecraftClient.getInstance().getNetworkHandler() != null;
    }

    public boolean IsConnected() {
        return isConnected();
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

    public int GetLatency() {
        return getLatency();
    }

    @Override @NotNull public LuaSignal getPacketSend() { return PacketSend; }
    @Override @NotNull public LuaSignal getPacketReceive() { return PacketReceive; }
    @Override @NotNull public LuaSignal getConnectedSignal() { return ConnectedSignal; }
    @Override @NotNull public LuaSignal getDisconnectedSignal() { return DisconnectedSignal; }

    public void unregisterListeners(@NotNull final String scriptName) {

    }

    public void clearAllListeners() {

    }
}