package pl.caltonek.avalanche.mixin.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.caltonek.avalanche.client.AvalancheClient;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Inject(method = "clickSlot", at = @At("HEAD"))
    private void onClickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        LuaTable slotData = new LuaTable();
        slotData.set("syncId", syncId);
        slotData.set("slot", slotId);
        slotData.set("button", button);
        slotData.set("actionType", actionType.name());

        AvalancheClient.getEventManager().post("inventory.slots.onslotclick", slotData);
        AvalancheClient.getEventManager().post("player.inventory.onslotclick", slotData);
    }

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        AvalancheClient.getEventManager().post("player.combat.onattackentity", target);
        AvalancheClient.getEventManager().post("player.combat.onattack", target);
    }

    @Inject(method = "interactItem", at = @At("HEAD"))
    private void onInteractItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        AvalancheClient.getEventManager().post("player.interaction.onuseitem", hand.name());
    }

    @Inject(method = "breakBlock", at = @At("RETURN"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            LuaTable event = avalanche$createPosEvent(pos);
            AvalancheClient.getEventManager().post("world.blocks.onblockbreak", event);
        }
    }

    @Inject(method = "interactBlock", at = @At("RETURN"))
    private void onInteractBlock( ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue().isAccepted()) {
            BlockPos placedPos = hitResult.getBlockPos().offset(hitResult.getSide());
            LuaTable event = avalanche$createPosEvent(placedPos);
            AvalancheClient.getEventManager().post("world.blocks.onblockplace", event);
            AvalancheClient.getEventManager().post("player.interaction.oninteractblock", event);
        }
    }

    @Unique
    private LuaTable avalanche$createPosEvent(BlockPos pos) {
        LuaTable event = new LuaTable();
        event.set("getX", new ZeroArgFunction() {
            @Override public LuaValue call() { return LuaValue.valueOf(pos.getX()); }
        });
        event.set("getY", new ZeroArgFunction() {
            @Override public LuaValue call() { return LuaValue.valueOf(pos.getY()); }
        });
        event.set("getZ", new ZeroArgFunction() {
            @Override public LuaValue call() { return LuaValue.valueOf(pos.getZ()); }
        });
        return event;
    }
}