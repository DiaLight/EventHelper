package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public abstract class PlayerInventoryBc {

    private static final TreeMap<Integer, Constructor<? extends PlayerInventoryBc>> backwardCompatibilityMap = new TreeMap<>();

    static {
        for (int minor = 8; minor < 32; minor++) {
            final String classPath = PlayerInventoryBc.class.getName() + minor;
            try {
                Class<? extends PlayerInventoryBc> act = (Class<? extends PlayerInventoryBc>) Class.forName(classPath);
                final Constructor<? extends PlayerInventoryBc> constructor = act.getConstructor(PlayerInventory.class);
                backwardCompatibilityMap.put(minor, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {}
        }
    }

    protected final PlayerInventory inventory;

    public PlayerInventoryBc(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public abstract ItemStack getItemInMainHand();

    public abstract void setItemInMainHand(ItemStack item);

    public static PlayerInventoryBc of(PlayerInventory inventory) {
        Map.Entry<Integer, Constructor<? extends PlayerInventoryBc>> entry = backwardCompatibilityMap.floorEntry(ReflectionUtils.MINOR_VERSION);
        if(entry == null) throw new RuntimeException("Unsupported version");
        final Constructor<? extends PlayerInventoryBc> constructor = entry.getValue();
        return ReflectionUtils.newInstance(constructor, inventory);
    }
}
