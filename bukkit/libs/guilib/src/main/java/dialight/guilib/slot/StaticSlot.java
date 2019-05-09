package dialight.guilib.slot;

import org.bukkit.inventory.ItemStack;

public class StaticSlot implements Slot {

    private final ItemStack itemStack;

    public StaticSlot(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override public ItemStack createItem() {
        return itemStack;
    }

    @Override
    public void onClick(SlotClickEvent e) {}

}
