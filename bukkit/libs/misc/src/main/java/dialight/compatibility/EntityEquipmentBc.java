package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public abstract class EntityEquipmentBc {

    private static final TreeMap<Integer, Constructor<? extends EntityEquipmentBc>> backwardCompatibilityMap = new TreeMap<>();

    static {
        for (int minor = 8; minor < 32; minor++) {
            final String classPath = EntityEquipmentBc.class.getName() + minor;
            try {
                Class<? extends EntityEquipmentBc> act = (Class<? extends EntityEquipmentBc>) Class.forName(classPath);
                final Constructor<? extends EntityEquipmentBc> constructor = act.getConstructor(EntityEquipment.class);
                backwardCompatibilityMap.put(minor, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {}
        }
    }

    protected final EntityEquipment equipment;

    public EntityEquipmentBc(EntityEquipment equipment) {
        this.equipment = equipment;
    }

    public abstract ItemStack getItemInMainHand();

    public abstract void setItemInMainHand(ItemStack item);

    public static EntityEquipmentBc of(EntityEquipment equipment) {
        Map.Entry<Integer, Constructor<? extends EntityEquipmentBc>> entry = backwardCompatibilityMap.floorEntry(ReflectionUtils.MINOR_VERSION);
        if(entry == null) throw new RuntimeException("Unsupported version");
        final Constructor<? extends EntityEquipmentBc> constructor = entry.getValue();
        return ReflectionUtils.newInstance(constructor, equipment);
    }

}
