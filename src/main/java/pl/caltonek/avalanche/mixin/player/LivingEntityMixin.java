package pl.caltonek.avalanche.mixin.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "setSprinting", at = @At("HEAD"))
    private void onSprint(boolean sprinting, CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity) {
            AvalancheClient.getEventManager().post("player.movement.onsprint", sprinting);

            AvalancheClient.getEventManager().post("multiplayer.playerstate.onsprintchange", sprinting);
        }
    }

    @Inject(method = "jump", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity) {
            AvalancheClient.getEventManager().post("player.movement.onjump", ci);
        }
    }
}
