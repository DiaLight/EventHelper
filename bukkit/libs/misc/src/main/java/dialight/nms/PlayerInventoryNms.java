package dialight.nms;

import org.bukkit.inventory.Inventory;

import java.lang.reflect.Constructor;

public abstract class PlayerInventoryNms {

    private static final Constructor<? extends PlayerInventoryNms> constructor =
            ReflectionUtils.findCompatibleConstructor(PlayerInventoryNms.class, Inventory.class);

    protected final Inventory inv;

    public PlayerInventoryNms(Inventory inv) {
        this.inv = inv;
    }

    public static PlayerInventoryNms of(Inventory inv) {
        return ReflectionUtils.newInstance(constructor, inv);
    }

    public abstract NbtTagListNms getNbtContent();
    public abstract void setNbtContent(NbtTagListNms inv);

}
