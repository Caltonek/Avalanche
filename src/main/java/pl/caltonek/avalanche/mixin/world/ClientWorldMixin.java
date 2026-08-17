package pl.caltonek.avalanche.mixin.world;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Inject(method = "addEntity", at = @At("TAIL"))
    private void onEntitySpawn(Entity entity, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("world.entities.onentityspawn", entity);
    }

    @Inject(method = "removeEntity", at = @At("TAIL"))
    private void onEntityDespawn(int entityId, Entity.RemovalReason reason, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("world.entities.onentitydespawn", entityId);
    }

    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void onBlockChange(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            AvalancheClient.getEventManager().post("world.blocks.onblockchange", pos);
        }
    }
}