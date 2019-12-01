package dialight.nms;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import java.lang.reflect.Field;

public class ItemStackNms8 extends ItemStackNms {


    private static final Field f_CraftItemStack_handle;

    static {
        try {
            f_CraftItemStack_handle = CraftItemStack.class.getDeclaredField("handle");
            f_CraftItemStack_handle.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStackNms8(org.bukkit.inventory.ItemStack item) {
        super(item);
    }

    public CraftItemStack asCraft() {
        if (item instanceof CraftItemStack) {
            return (CraftItemStack) item;
        } else {
            return CraftItemStack.asCraftCopy(item);
        }
    }

    public ItemStack asNms() {
        return (ItemStack) getHandle();
    }

    @Override public Object getHandle() {
        try {
            return f_CraftItemStack_handle.get(asCraft());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public org.bukkit.inventory.ItemStack mergeNbt(NbtTagCompoundNms nbt) {
        NbtTagCompoundNms oldNbt = getNbt();
        if(oldNbt == null) {
            asNms().setTag((NBTTagCompound) nbt.getNms());
            return item;
        }
        NbtTagCompoundNms mergedNbt = oldNbt.merge(nbt);
//        ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        ItemStack itemStack = asNms();
        itemStack.setTag((NBTTagCompound) mergedNbt.getNms());
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override public NbtTagCompoundNms getNbt() {
        NbtTagCompoundNms nbt = NbtTagCompoundNms.of(asNms().getTag());
        return nbt.clone();  // for ReflectionUtils.MINOR_VERSION < 13
    }
    @Override public NbtTagCompoundNms serialize() {
        NBTTagCompound out = new NBTTagCompound();
        asNms().save(out);
        return NbtTagCompoundNms.of(out);
    }

    public static ItemStackNms fromNbt(NbtTagCompoundNms nbt) {
        return new ItemStackNms8(CraftItemStack.asBukkitCopy(ItemStack.createStack((NBTTagCompound) nbt.getNms())));
    }

}
