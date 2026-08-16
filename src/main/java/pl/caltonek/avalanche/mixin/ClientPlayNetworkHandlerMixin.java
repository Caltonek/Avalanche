package pl.caltonek.avalanche.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.caltonek.avalanche.api.event.ChatSendEvent;
import pl.caltonek.avalanche.api.service.impl.ChatServiceImpl;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Unique
    private static final Logger avalanche$LOGGER = LogManager.getLogger("Avalanche-Mixin");

    @Unique
    private static final ThreadLocal<Boolean> avalanche$IGNORE_RECURSION = ThreadLocal.withInitial(() -> false);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String content, CallbackInfo ci) {
        if (avalanche$IGNORE_RECURSION.get()) return;

        ChatServiceImpl chatService = AvalancheClient.getChatService();
        if (chatService.getChatSendListeners().isEmpty()) return;

        final ChatSendEvent event = new ChatSendEvent(content);
        final LuaTable luaEvent = createLuaEventWrapper(event);

        for (LuaValue listener : chatService.getChatSendListeners()) {
            try {
                listener.call(luaEvent);
            } catch (Exception exception) {
                avalanche$LOGGER.error("Error executing Lua chat send listener:", exception);
            }
        }

        LuaValue updatedMessage = luaEvent.get("message");
        if (!updatedMessage.isnil()) {
            event.setMessage(updatedMessage.tojstring());
        }

        if (event.isCancelled()) {
            ci.cancel();
            return;
        }

        if (!event.getMessage().equals(content)) {
            ci.cancel();
            avalanche$IGNORE_RECURSION.set(true);
            try {
                var handler = (ClientPlayNetworkHandler) (Object) this;
                handler.sendChatMessage(event.getMessage());
            } finally {
                avalanche$IGNORE_RECURSION.set(false);
            }
        }
    }

    @Unique
    private LuaTable createLuaEventWrapper(final ChatSendEvent event) {
        final LuaTable wrapper = new LuaTable();
        wrapper.set("message", LuaValue.valueOf(event.getMessage()));

        wrapper.set("getMessage", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return wrapper.get("message");
            }
        });

        wrapper.set("setMessage", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String newMessage = arg.tojstring();
                wrapper.set("message", LuaValue.valueOf(newMessage));
                event.setMessage(newMessage);
                return LuaValue.NIL;
            }
        });

        wrapper.set("cancel", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                event.cancel();
                return LuaValue.NIL;
            }
        });

        return wrapper;
    }
}