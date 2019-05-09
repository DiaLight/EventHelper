package dialight.compatibility;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityEquipmentBc8 extends EntityEquipmentBc {

    public EntityEquipmentBc8(EntityEquipment equipment) {
        super(equipment);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return equipment.getItemInHand();
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        equipment.setItemInHand(item);
    }

}
