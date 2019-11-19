package dialight.compatibility;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractAtEntityEventBc9 extends PlayerInteractAtEntityEventBc {

    public PlayerInteractAtEntityEventBc9(PlayerInteractAtEntityEvent event) {
        super(event);
    }

    @Override
    public ItemStack getUsedItem() {
        EntityEquipment equipment = event.getPlayer().getEquipment();
        if(equipment == null) return null;
        switch (event.getHand()) {
            case HAND:
                return equipment.getItemInMainHand();
            case OFF_HAND:
                return equipment.getItemInOffHand();
        }
        return null;
    }

}
