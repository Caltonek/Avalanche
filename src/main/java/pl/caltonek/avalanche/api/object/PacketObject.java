package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public record PacketObject(
        @NotNull String type,
        byte[] data
) {
    @NotNull public String getType() { return type; }
    public byte[] getData() { return data; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PacketObject that)) return false;
        return type.equals(that.type) && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}