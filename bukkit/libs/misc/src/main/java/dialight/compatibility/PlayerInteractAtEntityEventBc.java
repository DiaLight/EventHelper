package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public abstract class PlayerInteractAtEntityEventBc {
    
    private static final TreeMap<Integer, Constructor<? extends PlayerInteractAtEntityEventBc>> backwardCompatibilityMap = new TreeMap<>();

    static {
        for (int minor = 8; minor < 32; minor++) {
            final String classPath = PlayerInteractAtEntityEventBc.class.getName() + minor;
            try {
                Class<? extends PlayerInteractAtEntityEventBc> act = (Class<? extends PlayerInteractAtEntityEventBc>) Class.forName(classPath);
                final Constructor<? extends PlayerInteractAtEntityEventBc> constructor = act.getConstructor(PlayerInteractAtEntityEvent.class);
                backwardCompatibilityMap.put(minor, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {}
        }
    }

    protected final PlayerInteractAtEntityEvent event;

    public PlayerInteractAtEntityEventBc(PlayerInteractAtEntityEvent event) {
        this.event = event;
    }

    public abstract ItemStack getUsedItem();

    public static PlayerInteractAtEntityEventBc of(PlayerInteractAtEntityEvent event) {
        Map.Entry<Integer, Constructor<? extends PlayerInteractAtEntityEventBc>> entry = backwardCompatibilityMap.floorEntry(ReflectionUtils.MINOR_VERSION);
        if(entry == null) throw new RuntimeException("Unsupported version");
        final Constructor<? extends PlayerInteractAtEntityEventBc> constructor = entry.getValue();
        return ReflectionUtils.newInstance(constructor, event);
    }
    
    
}
