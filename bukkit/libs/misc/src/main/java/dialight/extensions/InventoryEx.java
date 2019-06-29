package dialight.extensions;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryEx {

    private final Inventory inv;

    private InventoryEx(Inventory inv) {
        this.inv = inv;
    }

    public boolean addToEmptySlot(ItemStack item) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack cur = inv.getItem(i);
            if(cur == null) {
                inv.setItem(i, item);
                return true;
            }
        }
        return false;
    }

    public static InventoryEx of(Inventory inv) {
        return new InventoryEx(inv);
    }

}
