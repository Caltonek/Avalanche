package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.WorldObject;
import pl.caltonek.avalanche.api.service.WorldService;
import pl.caltonek.avalanche.api.signal.LuaSignal;
import pl.caltonek.avalanche.exceptions.WorldServiceException;

public final class WorldServiceImpl implements WorldService {

    public final LuaSignal BlockChanged = new LuaSignal("world.blocks.onblockchange");
    public final LuaSignal BlockBroken = new LuaSignal("world.blocks.onblockbreak");
    public final LuaSignal BlockPlaced = new LuaSignal("world.blocks.onblockplace");
    public final LuaSignal ChunkLoaded = new LuaSignal("world.chunks.onchunkload");
    public final LuaSignal EntitySpawned = new LuaSignal("world.entities.onentityspawn");

    @Override
    @Nullable
    public WorldObject getCurrentWorld() {
        final var world = MinecraftClient.getInstance().world;
        if (world == null) {
            return null;
        }

        return new WorldObject(
                world.getRegistryKey().getValue().toString(),
                world.getTime()
        );
    }

    @Override @Nullable public WorldObject GetCurrentWorld() { return getCurrentWorld(); }

    @Override
    @NotNull
    public String getBlock(final int x, final int y, final int z) {
        final var world = MinecraftClient.getInstance().world;
        if (world == null) {
            throw new WorldServiceException("Cannot get block: client is not currently in a world.");
        }

        final BlockPos blockPos = new BlockPos(x, y, z);
        final var blockState = world.getBlockState(blockPos);

        return Registries.BLOCK.getId(blockState.getBlock()).toString();
    }

    @Override @NotNull public String GetBlock(int x, int y, int z) { return getBlock(x, y, z); }

    @Override
    public boolean isChunkLoaded(final int x, final int z) {
        final var world = MinecraftClient.getInstance().world;
        if (world == null) {
            return false;
        }

        return world.getChunkManager().isChunkLoaded(x >> 4, z >> 4);
    }

    @Override public boolean IsChunkLoaded(int x, int z) { return isChunkLoaded(x, z); }

    @Override
    public long getTime() {
        final var world = MinecraftClient.getInstance().world;
        return world != null ? world.getTime() : 0L;
    }

    @Override public long GetTime() { return getTime(); }

    @Override @NotNull public LuaSignal getBlockChanged() { return BlockChanged; }
    @Override @NotNull public LuaSignal getBlockBroken() { return BlockBroken; }
    @Override @NotNull public LuaSignal getBlockPlaced() { return BlockPlaced; }
    @Override @NotNull public LuaSignal getChunkLoaded() { return ChunkLoaded; }
    @Override @NotNull public LuaSignal getEntitySpawned() { return EntitySpawned; }
}