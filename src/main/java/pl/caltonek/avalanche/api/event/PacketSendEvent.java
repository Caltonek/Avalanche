package pl.caltonek.avalanche.api.event;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.object.PacketObject;

public final class PacketSendEvent extends CancellableEvent {

    private PacketObject packet;

    public PacketSendEvent(@NotNull final PacketObject packet) {
        this.packet = packet;
    }

    @NotNull public PacketObject getPacket() { return packet; }
    @NotNull public String getType() { return packet.getType(); }
    public void replace(@NotNull final PacketObject packet) { this.packet = packet; }
}