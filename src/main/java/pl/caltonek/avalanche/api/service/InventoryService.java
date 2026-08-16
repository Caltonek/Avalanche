package pl.caltonek.avalanche.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.avalanche.api.object.ItemObject;

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
}