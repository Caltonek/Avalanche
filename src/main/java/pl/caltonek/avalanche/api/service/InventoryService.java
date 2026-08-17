package pl.caltonek.avalanche.api.service;

import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.ItemObject;
import pl.caltonek.avalanche.api.signal.LuaSignal;

import java.util.List;

public interface InventoryService {

    @Nullable ItemObject getItem(final int slot);
    @Nullable ItemObject getMainHand();
    @Nullable ItemObject getOffHand();
    int getSelectedSlot();
    void setSelectedSlot(final int slot);

    @NotNull List<ItemObject> getItems();
    boolean hasItem(@NotNull final String itemId);
    int countItem(@NotNull final String itemId);

    void clickSlot(int syncId, int slot, int button, SlotActionType actionType);
    void dropItem(int slot, boolean dropAll);
    void quickMove(int slot);

    @Nullable ItemObject GetItem(final int slot);
    @Nullable ItemObject GetMainHand();
    @Nullable ItemObject GetOffHand();
    int GetSelectedSlot();
    void SetSelectedSlot(final int slot);
    @NotNull List<ItemObject> GetItems();
    boolean HasItem(@NotNull final String itemId);
    int CountItem(@NotNull final String itemId);
    void ClickSlot(int syncId, int slot, int button, SlotActionType actionType);
    void DropItem(int slot, boolean dropAll);
    void QuickMove(int slot);

    @NotNull LuaSignal getOpen();
    @NotNull LuaSignal getClose();
    @NotNull LuaSignal getSlotClick();
    @NotNull LuaSignal getItemPickup();
    @NotNull LuaSignal getItemDrop();
}