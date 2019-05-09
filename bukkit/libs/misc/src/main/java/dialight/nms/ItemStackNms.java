package dialight.nms;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ItemStackNms {

    private static final Class<?> CraftItemStack_class = ReflectionUtils.getCraftbukkitClass("inventory.CraftItemStack");
    private static final Class<?> ItemStack_class = ReflectionUtils.getNMSClass("ItemStack");

    private static Object cloneItemStack(Object is) {
        return ReflectionUtils.invokeMethod(is, "cloneItemStack", new Class[0]);
    }

    private static Object asNMSCopy(ItemStack itemStack) {
        Object is = ReflectionUtils.invokeMethod(CraftItemStack_class, null, "asNMSCopy",
                new Class[]{ItemStack.class}, itemStack);
//        if(ReflectionUtils.MINOR_VERSION < 13) return cloneItemStack(is);
        return is;
    }

    private static Object getNMS(ItemStack itemStack) {
        return ReflectionUtils.getFieldValue(itemStack, "handle");
    }

    private static Object getOBCCopy(ItemStack itemStack) {
        return ReflectionUtils.invokeMethod(CraftItemStack_class, null, "asCraftCopy",
                new Class[]{ItemStack.class}, itemStack);
    }

    private static ItemStack asBukkitCopy(Object nmsItem) {
//        if(ReflectionUtils.MINOR_VERSION < 13) {
//            return (ItemStack) ReflectionUtils.newInstance(CraftItemStack_class,
//                    new Class[]{ItemStack_class}, nmsItem);
//        }
        return (ItemStack) ReflectionUtils.invokeMethod(CraftItemStack_class, null, "asBukkitCopy",
                new Class[]{ItemStack_class}, nmsItem);
    }

    private static ItemStack setNBTTag(NBTTagCompoundNms nbt, ItemStack itemStack) {
        Object nmsItem = asNMSCopy(itemStack);
        ReflectionUtils.invokeMethod(nmsItem, "setTag",
                new Class[]{NBTTagCompoundNms.NMS_class}, nbt.getInternal());
        return asBukkitCopy(nmsItem);
    }

    private static ItemStack mergeNBTTag(NBTTagCompoundNms tag, ItemStack itemStack) {
        NBTTagCompoundNms oldNbt = getNbtTag(itemStack);
        if(oldNbt == null) return setNBTTag(tag, itemStack);
        NBTTagCompoundNms mergedNbt = oldNbt.merge(tag);
        return setNBTTag(mergedNbt, itemStack);
    }

    @Nullable public static NBTTagCompoundNms getNbtTag(ItemStack itemStack) {
        Object nmsItem = asNMSCopy(itemStack);
        Object nbt = ReflectionUtils.invokeMethod(nmsItem, "getTag", new Class[0]);
        if(nbt == null) return null;
        NBTTagCompoundNms wrap = new NBTTagCompoundNms(nbt);
//        if(ReflectionUtils.MINOR_VERSION < 13) return wrap.clone();
        return wrap;
    }

    public static ItemStack applyNbt(ItemStack is, String json) {
        NBTTagCompoundNms nbt = NBTTagCompoundNms.deserializeFromString(json);
        return mergeNBTTag(nbt, is);
    }

    public static void dump(ItemStack is) {
        for (Map.Entry<String, Object> entry : is.serialize().entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("  nbt: " + ItemStackNms.getNbtTag(is));
    }
}
