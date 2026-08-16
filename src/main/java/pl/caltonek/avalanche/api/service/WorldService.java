package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.WorldObject;

public interface WorldService {
    @Nullable WorldObject getCurrentWorld();
    @NotNull String getBlock(final int x, final int y, final int z);
    boolean isChunkLoaded(final int x, final int z);
    long getTime();
}