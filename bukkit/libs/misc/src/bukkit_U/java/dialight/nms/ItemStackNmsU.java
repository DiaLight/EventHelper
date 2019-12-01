package dialight.nms;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ItemStackNmsU {

    public static final Class<?> NBTTagCompound_class;
    private static final Class<?> CraftItemStack_class = ReflectionUtils.getOBCClass("inventory.CraftItemStack");
    private static final Class<?> ItemStack_class = ReflectionUtils.getNMSClass("ItemStack");
    private static final Field f_CraftItemStack_handle;


    static {
        NBTTagCompound_class = ReflectionUtils.getNMSClass("NBTTagCompound");
        try {
            f_CraftItemStack_handle = CraftItemStack_class.getDeclaredField("handle");
            f_CraftItemStack_handle.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    org.bukkit.inventory.ItemStack item;
    public ItemStackNmsU(org.bukkit.inventory.ItemStack item) {
        this.item = item;
    }

//    public ItemStack asNms() {
//        return (ItemStack) getHandle();
//    }

//    public Object getHandle() {
//        try {
//            return f_CraftItemStack_handle.get(asCraft());
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static Object cloneItemStack(Object is) {
        return ReflectionUtils.invokeMethod(is, "cloneItemStack", new Class[0]);
    }

    private static Object asNMSCopy(org.bukkit.inventory.ItemStack itemStack) {
        Object is = ReflectionUtils.invokeMethod(CraftItemStack_class, null, "asNMSCopy",
                new Class[]{org.bukkit.inventory.ItemStack.class}, itemStack);
//        if(ReflectionUtils.MINOR_VERSION < 13) return cloneItemStack(is);
        return is;
    }

    private static Object getNMS(org.bukkit.inventory.ItemStack itemStack) {
        return ReflectionUtils.getFieldValue(itemStack, "handle");
    }

    private static Object getOBCCopy(org.bukkit.inventory.ItemStack itemStack) {
        return ReflectionUtils.invokeMethod(CraftItemStack_class, null, "asCraftCopy",
                new Class[]{org.bukkit.inventory.ItemStack.class}, itemStack);
    }

    private static org.bukkit.inventory.ItemStack asBukkitCopy(Object nmsItem) {
//        if(ReflectionUtils.MINOR_VERSION < 13) {
//            return (ItemStack) ReflectionUtils.newInstance(CraftItemStack_class,
//                    new Class[]{ItemStack_class}, nmsItem);
//        }
        return (org.bukkit.inventory.ItemStack) ReflectionUtils.invokeMethod(CraftItemStack_class, null, "asBukkitCopy",
                new Class[]{ItemStack_class}, nmsItem);
    }

    private static org.bukkit.inventory.ItemStack setNBTTag(NbtTagCompoundNms nbt, org.bukkit.inventory.ItemStack itemStack) {
        Object nmsItem = asNMSCopy(itemStack);
        ReflectionUtils.invokeMethod(nmsItem, "setTag",
                new Class[]{NBTTagCompound_class}, nbt.getNms());
        return asBukkitCopy(nmsItem);
    }

    private static org.bukkit.inventory.ItemStack mergeNBTTag(NbtTagCompoundNms tag, org.bukkit.inventory.ItemStack itemStack) {
        NbtTagCompoundNms oldNbt = getNbtTag(itemStack);
        if(oldNbt == null) return setNBTTag(tag, itemStack);
        NbtTagCompoundNms mergedNbt = oldNbt.merge(tag);
        return setNBTTag(mergedNbt, itemStack);
    }

    @Nullable
    public static NbtTagCompoundNms getNbtTag(org.bukkit.inventory.ItemStack itemStack) {
        Object nmsItem = asNMSCopy(itemStack);
        Object nbt = ReflectionUtils.invokeMethod(nmsItem, "getTag", new Class[0]);
        if(nbt == null) return null;
        NbtTagCompoundNms wrap = NbtTagCompoundNms.of(nbt);
//        if(ReflectionUtils.MINOR_VERSION < 13) return wrap.clone();
        return wrap;
    }

//    public static org.bukkit.inventory.ItemStack applyNbt(org.bukkit.inventory.ItemStack is, String json) {
//        NbtTagCompoundNms nbt = NbtTagCompoundNms.deserializeFromString(json);
//        return mergeNBTTag(nbt, is);
//    }
//
//    public static void dump(org.bukkit.inventory.ItemStack is) {
//        for (Map.Entry<String, Object> entry : is.serialize().entrySet()) {
//            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
//        }
//        System.out.println("  nbt: " + ItemStackNms8.getNbtTag(is));
//    }


}
