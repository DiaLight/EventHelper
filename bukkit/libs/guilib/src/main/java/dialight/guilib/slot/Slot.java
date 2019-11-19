package dialight.guilib.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Slot {

    void onClick(SlotClickEvent e);
    @NotNull ItemStack createItem();

    default void attached(SlotUsage usage) {}
    default void detached(SlotUsage usage) {}

}
