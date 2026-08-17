package pl.caltonek.avalanche.mixin.network;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("network.packets.onpacketsendpre", packet);
        AvalancheClient.getEventManager().post("network.packets.onpacketsend", packet);
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("TAIL"))
    private void onSendPacketPost(Packet<?> packet, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("network.packets.onpacketsendpost", packet);
    }

    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static <T extends PacketListener> void onReceivePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("network.packets.onpacketreceivepre", packet);
        AvalancheClient.getEventManager().post("network.packets.onpacketreceive", packet);
    }

    @Inject(method = "handlePacket", at = @At("TAIL"))
    private static <T extends PacketListener> void onReceivePacketPost(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("network.packets.onpacketreceivepost", packet);
    }

    @Inject(method = "handleDisconnection", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        AvalancheClient.getEventManager().post("network.connection.ondisconnect", ci);
    }
}