package dialight.compatibility;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryBc9 extends PlayerInventoryBc {

    public PlayerInventoryBc9(PlayerInventory inventory) {
        super(inventory);
    }

    @Override public ItemStack getItemInMainHand() {
        return inventory.getItemInMainHand();
    }

    @Override public void setItemInMainHand(ItemStack item) {
        inventory.setItemInMainHand(item);
    }

}
