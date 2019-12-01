package dialight.nms;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Method;
import java.util.UUID;

public class OfflinePlayerNms {

    private static final Class<?> CraftOfflinePlayer_class = ReflectionUtils.getOBCClass("CraftOfflinePlayer");

    // Reflection cache
    private static Method m_CraftOfflinePlayer_getData;

    public static NbtTagCompoundNms getData_old(OfflinePlayer op) {
        try {
            Class<? extends OfflinePlayer> clazz = op.getClass();
            if (!CraftOfflinePlayer_class.isAssignableFrom(clazz)) throw new ClassCastException("class CraftOfflinePlayer is not assignable from " + clazz);
            if(m_CraftOfflinePlayer_getData == null) {
                m_CraftOfflinePlayer_getData = CraftOfflinePlayer_class.getDeclaredMethod("getData");
                m_CraftOfflinePlayer_getData.setAccessible(true);
            }
            Object data = m_CraftOfflinePlayer_getData.invoke(op);
            if(data == null) return null;
            return NbtTagCompoundNms.of(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*private final CraftInventory converter = new CraftInventory(null);

    public ItemStack[] getInventoryContents(UUID uuid) {
        val p = server.getPlayer(uuid);
        if (p != null) return p.getInventory().getContents();
        val nbtManager = (ServerNBTManager) MinecraftServer.getServer().getWorldServer(0).dataManager;
        val data = nbtManager.getPlayerData(uuid.toString());
        if (data == null) return null;
        val nbt = data.getList("Inventory", 10);
        if (nbt == null) return null;
        val virtual = new PlayerInventory(null);
        virtual.b(nbt);
        return converter.asCraftMirror(virtual.getContents());
    }

    public boolean setContents(UUID uuid, ItemStack[] contents) {
        val p = server.getPlayer(uuid);
        if (p != null) {
            p.getInventory().setContents(contents);
            return true;
        }
        val nbtManager = (ServerNBTManager) MinecraftServer.getServer().getWorldServer(0).dataManager;
        val data = nbtManager.getPlayerData(uuid.toString());
        if (data == null) return false;
        val _inventory = new PlayerInventory(null);
        val inventory = new CraftInventory(_inventory);
        inventory.setContents(contents);
        data.set("Inventory", _inventory.a(new NBTTagList()));
        try (val out = new FileOutputStream(new File(nbtManager.getPlayerDir(), uuid + ".dat"))) {
            NBTCompressedStreamTools.a(data, out);
        }
        return true;
    }*/
    @Nullable public static NbtTagCompoundNms getData(World world, UUID uuid) {
        try {
            File worldFolder = world.getWorldFolder();
            File playerdata = new File(worldFolder, "playerdata");
            if(!playerdata.exists()) playerdata.mkdirs();
            File datfile = new File(playerdata, uuid + ".dat");
            if(!datfile.exists()) return null;
            return NbtTagCompoundNms.deserialize(new FileInputStream(datfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setData(World world, UUID uuid, NbtTagCompoundNms nbt) {
        try {
            File worldFolder = world.getWorldFolder();
            File playerdata = new File(worldFolder, "playerdata");
            if(!playerdata.exists()) playerdata.mkdirs();
            File tmpfile = new File(playerdata, uuid + ".dat.tmp");
            File datfile = new File(playerdata, uuid + ".dat");
            nbt.serialize(new FileOutputStream(tmpfile));
            if (datfile.exists()) datfile.delete();
            tmpfile.renameTo(datfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
