package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.WorldObject;
import pl.caltonek.avalanche.api.service.WorldService;
import pl.caltonek.avalanche.exceptions.WorldServiceException;

public final class WorldServiceImpl implements WorldService {

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

    @Override
    public boolean isChunkLoaded(final int x, final int z) {
        final var world = MinecraftClient.getInstance().world;
        if (world == null) {
            return false;
        }

        return world.getChunkManager().isChunkLoaded(x >> 4, z >> 4);
    }

    @Override
    public long getTime() {
        final var world = MinecraftClient.getInstance().world;
        return world != null ? world.getTime() : 0L;
    }
}