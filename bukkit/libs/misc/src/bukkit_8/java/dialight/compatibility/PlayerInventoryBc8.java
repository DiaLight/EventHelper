package dialight.compatibility;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryBc8 extends PlayerInventoryBc {

    public PlayerInventoryBc8(PlayerInventory inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return inventory.getItemInHand();
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        inventory.setItemInHand(item);
    }

}
