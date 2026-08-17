package pl.caltonek.avalanche.mixin.minecraft;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.api.service.impl.MinecraftServiceImpl;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onClientTick(CallbackInfo ci) {
        AvalancheClient.getEventManager().post("minecraft.lifecycle.onclienttick", ci);
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        var client = MinecraftClient.getInstance();

        if (client.currentScreen != null && screen == null) {
            AvalancheClient.getEventManager().post("minecraft.screen.onscreenclose", client.currentScreen);
            AvalancheClient.getEventManager().post("inventory.lifecycle.onclose", client.currentScreen);
        } else if (screen != null) {
            AvalancheClient.getEventManager().post("minecraft.screen.onscreenopen", screen);
            AvalancheClient.getEventManager().post("inventory.lifecycle.onopen", screen);
        }

        AvalancheClient.getEventManager().post("minecraft.screen.onscreenchange", screen);
    }

    @Inject(method = "joinWorld", at = @At("HEAD"))
    private void onJoinWorld(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("minecraft.lifecycle.ongamejoin", world);
        AvalancheClient.getEventManager().post("world.lifecycle.onworldload", world);
    }

    @Inject(method = "disconnect()V", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;

        AvalancheClient.getEventManager().post("minecraft.lifecycle.ondisconnect", currentScreen);
        AvalancheClient.getEventManager().post("world.lifecycle.onworldunload", currentScreen);

        if (AvalancheClient.getMinecraftService() instanceof MinecraftServiceImpl impl) {
            impl.clearAllScriptListeners();
        }
    }
}