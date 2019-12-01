package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Constructor;

public abstract class PlayerInventoryBc {

    private static final Constructor<? extends PlayerInventoryBc> constructor =
            ReflectionUtils.findCompatibleConstructor(PlayerInventoryBc.class, PlayerInventory.class);

    protected final PlayerInventory inventory;

    public PlayerInventoryBc(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public abstract ItemStack getItemInMainHand();

    public abstract void setItemInMainHand(ItemStack item);

    public static PlayerInventoryBc of(PlayerInventory inventory) {
        return ReflectionUtils.newInstance(constructor, inventory);
    }

}
