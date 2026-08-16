package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.api.object.PacketObject;

public interface NetworkService {
    void sendPacket(@NotNull final PacketObject packet);
    void onSendPacket(@NotNull final LuaValue callback);
    void onReceivePacket(@NotNull final LuaValue callback);
}