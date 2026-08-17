package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.WorldObject;
import pl.caltonek.avalanche.api.signal.LuaSignal;

public interface WorldService {

    @Nullable WorldObject getCurrentWorld();
    @NotNull String getBlock(final int x, final int y, final int z);
    boolean isChunkLoaded(final int x, final int z);
    long getTime();

    @Nullable WorldObject GetCurrentWorld();
    @NotNull String GetBlock(final int x, final int y, final int z);
    boolean IsChunkLoaded(final int x, final int z);
    long GetTime();

    @NotNull LuaSignal getBlockChanged();
    @NotNull LuaSignal getBlockBroken();
    @NotNull LuaSignal getBlockPlaced();
    @NotNull LuaSignal getChunkLoaded();
    @NotNull LuaSignal getEntitySpawned();
}