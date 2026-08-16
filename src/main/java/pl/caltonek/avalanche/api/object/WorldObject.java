package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;

public record WorldObject(
        @NotNull String dimension,
        long time
) {
    @NotNull public String getDimension() { return dimension; }
    public long getTime() { return time; }
}