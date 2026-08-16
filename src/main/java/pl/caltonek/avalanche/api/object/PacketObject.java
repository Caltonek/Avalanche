package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;

public final class PacketObject {

    private final String type;
    private final byte[] data;

    public PacketObject(@NotNull final String type, @NotNull final byte[] data) {
        this.type = type;
        this.data = data;
    }

    @NotNull public String getType() { return type; }
    @NotNull public byte[] getData() { return data; }
}