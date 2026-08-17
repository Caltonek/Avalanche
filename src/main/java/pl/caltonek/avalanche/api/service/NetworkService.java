package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.object.PacketObject;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public interface NetworkService {

    void sendPacket(@NotNull final PacketObject packet);
    void sendCustomPayload(@NotNull final String channel, @NotNull final byte[] data);

    boolean isConnected();
    int getLatency();

    @NotNull LuaSignal getPacketSend();
    @NotNull LuaSignal getPacketReceive();
    @NotNull LuaSignal getConnectedSignal();
    @NotNull LuaSignal getDisconnectedSignal();
}