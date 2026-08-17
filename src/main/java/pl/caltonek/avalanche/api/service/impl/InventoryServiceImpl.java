package pl.caltonek.avalanche.api.service.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.ItemObject;
import pl.caltonek.avalanche.api.service.InventoryService;
import pl.caltonek.avalanche.api.signal.LuaSignal;
import pl.caltonek.avalanche.exceptions.InventoryServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InventoryServiceImpl implements InventoryService {

    public final LuaSignal Open = new LuaSignal("inventory.lifecycle.onopen");
    public final LuaSignal Close = new LuaSignal("inventory.lifecycle.onclose");
    public final LuaSignal SlotClick = new LuaSignal("inventory.slots.onslotclick");
    public final LuaSignal ItemPickup = new LuaSignal("inventory.items.onitempickup");
    public final LuaSignal ItemDrop = new LuaSignal("inventory.items.onitemdrop");

    @Override
    @Nullable
    public ItemObject getItem(final int slot) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) {
            throw new InventoryServiceException("Cannot get item: player is unavailable.");
        }

        final var inventory = player.getInventory();
        if (slot < 0 || slot >= inventory.size()) {
            throw new InventoryServiceException("Slot index out of bounds: " + slot + " (inventory size: " + inventory.size() + ").");
        }

        final ItemStack stack = inventory.getStack(slot);
        return mapToItemObject(stack, slot);
    }

    @Override @Nullable public ItemObject GetItem(int slot) { return getItem(slot); }

    @Override
    @Nullable
    public ItemObject getMainHand() {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) {
            throw new InventoryServiceException("Cannot get main hand item: player is unavailable.");
        }

        return mapToItemObject(player.getMainHandStack(), player.getInventory().selectedSlot);
    }

    @Override @Nullable public ItemObject GetMainHand() { return getMainHand(); }

    @Override
    @Nullable
    public ItemObject getOffHand() {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) {
            throw new InventoryServiceException("Cannot get offhand item: player is unavailable.");
        }

        return mapToItemObject(player.getOffHandStack(), 45);
    }

    @Override @Nullable public ItemObject GetOffHand() { return getOffHand(); }

    @Override
    public int getSelectedSlot() {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) {
            throw new InventoryServiceException("Cannot get selected slot: player is unavailable.");
        }
        return player.getInventory().selectedSlot;
    }

    @Override public int GetSelectedSlot() { return getSelectedSlot(); }

    @Override
    public void setSelectedSlot(final int slot) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) {
            throw new InventoryServiceException("Cannot set selected slot: player is unavailable.");
        }

        if (slot < 0 || slot > 8) {
            throw new InventoryServiceException("Invalid hotbar slot: " + slot + ". Must be between 0 and 8.");
        }

        player.getInventory().selectedSlot = slot;
    }

    @Override public void SetSelectedSlot(int slot) { setSelectedSlot(slot); }

    @Override
    @NotNull
    public List<ItemObject> getItems() {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) return Collections.emptyList();

        final List<ItemObject> items = new ArrayList<>();
        final var inventory = player.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            final ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                items.add(mapToItemObject(stack, i));
            }
        }
        return items;
    }

    @Override @NotNull public List<ItemObject> GetItems() { return getItems(); }

    @Override
    public void clickSlot(int syncId, int slot, int button, SlotActionType actionType) {
        final var client = MinecraftClient.getInstance();
        if (client.interactionManager != null && client.player != null) {
            client.interactionManager.clickSlot(syncId, slot, button, actionType, client.player);
        }
    }

    @Override public void ClickSlot(int syncId, int slot, int button, SlotActionType actionType) { clickSlot(syncId, slot, button, actionType); }

    @Override
    public void dropItem(int slot, boolean dropAll) {
        final var client = MinecraftClient.getInstance();
        if (client.player != null && client.interactionManager != null) {
            client.interactionManager.clickSlot(
                    client.player.currentScreenHandler.syncId,
                    slot,
                    dropAll ? 1 : 0,
                    SlotActionType.THROW,
                    client.player
            );
        }
    }

    @Override public void DropItem(int slot, boolean dropAll) { dropItem(slot, dropAll); }

    @Override
    public void quickMove(int slot) {
        final var client = MinecraftClient.getInstance();
        if (client.player != null && client.interactionManager != null) {
            client.interactionManager.clickSlot(
                    client.player.currentScreenHandler.syncId,
                    slot,
                    0,
                    SlotActionType.QUICK_MOVE,
                    client.player
            );
        }
    }

    @Override public void QuickMove(int slot) { quickMove(slot); }

    @Override
    public boolean hasItem(@NotNull final String itemId) {
        return countItem(itemId) > 0;
    }

    @Override public boolean HasItem(@NotNull String itemId) { return hasItem(itemId); }

    @Override
    public int countItem(@NotNull final String itemId) {
        final var player = MinecraftClient.getInstance().player;
        if (player == null) return 0;

        int totalCount = 0;
        final var inventory = player.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            final ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                final String id = Registries.ITEM.getId(stack.getItem()).toString();
                if (id.equalsIgnoreCase(itemId)) {
                    totalCount += stack.getCount();
                }
            }
        }
        return totalCount;
    }

    @Override public int CountItem(@NotNull String itemId) { return countItem(itemId); }

    @Override @NotNull public LuaSignal getOpen() { return Open; }
    @Override @NotNull public LuaSignal getClose() { return Close; }
    @Override @NotNull public LuaSignal getSlotClick() { return SlotClick; }
    @Override @NotNull public LuaSignal getItemPickup() { return ItemPickup; }
    @Override @NotNull public LuaSignal getItemDrop() { return ItemDrop; }

    @NotNull
    private ItemObject mapToItemObject(@NotNull final ItemStack stack, final int slot) {
        if (stack.isEmpty()) {
            return new ItemObject("minecraft:air", 0, 0, slot, "Air", 0, 0);
        }

        final String id = Registries.ITEM.getId(stack.getItem()).toString();
        final String name = stack.getName().getString();

        return new ItemObject(
                id,
                stack.getCount(),
                stack.getMaxCount(),
                slot,
                name,
                stack.getDamage(),
                stack.getMaxDamage()
        );
    }
}