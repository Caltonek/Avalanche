package pl.caltonek.avalanche.api.object;

import org.jetbrains.annotations.NotNull;

public final class WorldObject {

    private final String dimension;
    private final long time;

    public WorldObject(@NotNull final String dimension, final long time) {
        this.dimension = dimension;
        this.time = time;
    }

    @NotNull public String getDimension() { return dimension; }
    public long getTime() { return time; }
}