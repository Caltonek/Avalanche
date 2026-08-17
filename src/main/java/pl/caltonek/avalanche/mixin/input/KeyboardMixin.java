package pl.caltonek.avalanche.mixin.input;

import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.api.event.input.keyboard.*;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;

        var eventManager = AvalancheClient.getEventManager();

        if (action == GLFW.GLFW_PRESS) {
            KeyPressEvent event = new KeyPressEvent(key, scancode, modifiers);
            eventManager.post("minecraft.keyboard.onkeypress", event);
            if (event.isCancelled()) ci.cancel();
        } else if (action == GLFW.GLFW_RELEASE) {
            KeyReleaseEvent event = new KeyReleaseEvent(key, scancode, modifiers);
            eventManager.post("minecraft.keyboard.onkeyrelease", event);
            if (event.isCancelled()) ci.cancel();
        } else if (action == GLFW.GLFW_REPEAT) {
            KeyRepeatEvent event = new KeyRepeatEvent(key, scancode, modifiers);
            eventManager.post("minecraft.keyboard.onkeyrepeat", event);
            if (event.isCancelled()) ci.cancel();
        }
    }

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    private void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        CharTypedEvent event = new CharTypedEvent(codePoint, modifiers);
        AvalancheClient.getEventManager().post("minecraft.keyboard.onchartyped", event);
        if (event.isCancelled()) ci.cancel();
    }
}