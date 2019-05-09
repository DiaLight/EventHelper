package dialight.compatibility;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityEquipmentBc9 extends EntityEquipmentBc  {

    public EntityEquipmentBc9(EntityEquipment equipment) {
        super(equipment);
    }

    @Override public ItemStack getItemInMainHand() {
        return equipment.getItemInMainHand();
    }

    @Override public void setItemInMainHand(ItemStack item) {
        equipment.setItemInMainHand(item);
    }

}
