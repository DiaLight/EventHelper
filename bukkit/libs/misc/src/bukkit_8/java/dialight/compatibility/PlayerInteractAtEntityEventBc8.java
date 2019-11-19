package dialight.compatibility;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractAtEntityEventBc8 extends PlayerInteractAtEntityEventBc {

    public PlayerInteractAtEntityEventBc8(PlayerInteractAtEntityEvent event) {
        super(event);
    }

    @Override
    public ItemStack getUsedItem() {
        return event.getPlayer().getItemInHand();
    }

}
