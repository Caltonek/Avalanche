package pl.caltonek.avalanche.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.api.service.impl.NetworkServiceImpl;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        NetworkServiceImpl networkService = (NetworkServiceImpl) AvalancheClient.getMinecraftService().getNetwork();
        if (networkService.getSendPacketListeners().isEmpty()) return;

        LuaTable packetData = new LuaTable();
        packetData.set("type", packet.getClass().getSimpleName());

        for (LuaValue listener : networkService.getSendPacketListeners()) {
            try {
                listener.call(packetData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static <T extends PacketListener> void onReceivePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        NetworkServiceImpl networkService = (NetworkServiceImpl) AvalancheClient.getMinecraftService().getNetwork();
        if (networkService.getReceivePacketListeners().isEmpty()) return;

        LuaTable packetData = new LuaTable();
        packetData.set("type", packet.getClass().getSimpleName());

        for (LuaValue luaListener : networkService.getReceivePacketListeners()) {
            try {
                luaListener.call(packetData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}