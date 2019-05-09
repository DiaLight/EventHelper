package dialight.guilib.slot;

import dialight.guilib.layout.SlotLayout;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Slot {

    void onClick(SlotClickEvent e);
    @NotNull ItemStack createItem();

    default void attached(SlotLayout layout) {}

}
