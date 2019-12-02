package dialight.nms;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class ItemStackNms {

    private static final Constructor<? extends ItemStackNms> constructor;
    private static Method m_fromNbt;

    static {
        Class<? extends ItemStackNms> clazz = ReflectionUtils.findCompatibleClass(ItemStackNms.class);
        try {
            constructor = clazz.getConstructor(ItemStack.class);
            m_fromNbt = clazz.getDeclaredMethod("fromNbt", NbtTagCompoundNms.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final ItemStack item;

    public ItemStackNms(ItemStack item) {
        this.item = item;
    }

    public abstract NbtTagCompoundNms getNbt();

    public abstract NbtTagCompoundNms serialize();

    public static ItemStackNms fromNbt(NbtTagCompoundNms nbt) {
        return (ItemStackNms) ReflectionUtils.invokeMethod(m_fromNbt, null, nbt);
    }
    public abstract ItemStack mergeNbt(NbtTagCompoundNms nbt);

    public abstract Object getHandle();

    public static ItemStackNms of(ItemStack item) {
        return ReflectionUtils.newInstance(constructor, item);
    }


    public void dump() {
        for (Map.Entry<String, Object> entry : item.serialize().entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("  nbt: " + serialize().serializeToJson());
    }

    public ItemStack asBukkit() {
        return item;
    }
}
