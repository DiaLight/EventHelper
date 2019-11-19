package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public abstract class PlayerInteractAtEntityEventBc {

    private static final Constructor<? extends PlayerInteractAtEntityEventBc> constructor =
            ReflectionUtils.findCompatibleClass(PlayerInteractAtEntityEventBc.class, PlayerInteractAtEntityEvent.class);

    protected final PlayerInteractAtEntityEvent event;

    public PlayerInteractAtEntityEventBc(PlayerInteractAtEntityEvent event) {
        this.event = event;
    }

    public abstract ItemStack getUsedItem();

    public static PlayerInteractAtEntityEventBc of(PlayerInteractAtEntityEvent event) {
        return ReflectionUtils.newInstance(constructor, event);
    }
    
}
