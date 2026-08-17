package pl.caltonek.avalanche.mixin.input;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action == 1) {
            AvalancheClient.getEventManager().post("minecraft.mouse.onmousepress", button);
        } else if (action == 0) {
            AvalancheClient.getEventManager().post("minecraft.mouse.onmouserelease", button);
        }
    }

    @Inject(method = "onCursorPos", at = @At("HEAD"))
    private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("minecraft.mouse.onmousemove", ci);
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"))
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("minecraft.mouse.onmousescroll", vertical);
        AvalancheClient.getEventManager().post("inventory.hotbar.onscroll", vertical);
    }
}