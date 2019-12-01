package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public abstract class EntityEquipmentBc {

    private static final Constructor<? extends EntityEquipmentBc> constructor =
            ReflectionUtils.findCompatibleConstructor(EntityEquipmentBc.class, EntityEquipment.class);

    protected final EntityEquipment equipment;

    public EntityEquipmentBc(EntityEquipment equipment) {
        this.equipment = equipment;
    }

    public abstract ItemStack getItemInMainHand();

    public abstract void setItemInMainHand(ItemStack item);

    public static EntityEquipmentBc of(EntityEquipment equipment) {
        return ReflectionUtils.newInstance(constructor, equipment);
    }

}
