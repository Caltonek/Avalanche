package pl.caltonek.avalanche.mixin.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onPlayerTick(CallbackInfo ci) {
        AvalancheClient.getEventManager().post("minecraft.lifecycle.ontick", ci);
    }

    @Inject(method = "move", at = @At("HEAD"))
    private void onMove(MovementType type, Vec3d movement, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("player.movement.onmove", movement);
    }
}