package pl.caltonek.avalanche.mixin.chat;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.caltonek.avalanche.api.event.ChatSendEvent;
import pl.caltonek.avalanche.api.service.impl.CommandServiceImpl;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Unique
    private boolean avalanche$ignoreNextChat = false;

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void onSendCommand(String command, CallbackInfoReturnable<Boolean> cir) {
        AvalancheClient.getEventManager().post("chat.outgoing.oncommandsend", command);
        AvalancheClient.getEventManager().post("chat.commands.oncommand", command);

        String firstWord = command.contains(" ") ? command.substring(0, command.indexOf(" ")) : command;

        if (AvalancheClient.getMinecraftService().getCommand() instanceof CommandServiceImpl commandService) {
            if (commandService.isRegistered(firstWord)) {
                cir.setReturnValue(true);
                String args = command.contains(" ") ? command.substring(command.indexOf(" ") + 1) : "";
                commandService.executeCommand(firstWord, args);
            }
        }
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String content, CallbackInfo ci) {
        if (this.avalanche$ignoreNextChat) return;

        ChatSendEvent event = new ChatSendEvent(content);
        AvalancheClient.getEventManager().post("chat.outgoing.onchatsend", event);
        AvalancheClient.getEventManager().post("chat.outgoing.onmessagesend", event);

        if (event.isCancelled()) {
            ci.cancel();
            return;
        }

        if (!event.getMessage().equals(content)) {
            ci.cancel();
            this.avalanche$ignoreNextChat = true;
            try {
                ((ClientPlayNetworkHandler) (Object) this).sendChatMessage(event.getMessage());
            } finally {
                this.avalanche$ignoreNextChat = false;
            }
        }
    }

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("chat.incoming.onchatreceive", packet.content().getString());
        if (packet.overlay()) {
            AvalancheClient.getEventManager().post("chat.incoming.onactionbar", packet.content().getString());
        } else {
            AvalancheClient.getEventManager().post("chat.incoming.onsystemmessage", packet.content().getString());
        }
    }

    @Inject(method = "onPlayerList", at = @At("HEAD"))
    private void onPlayerList(PlayerListS2CPacket packet, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("multiplayer.list.onplayerlistupdate", packet);
    }
}