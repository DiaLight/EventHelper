package dialight.nms;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class OfflinePlayerNms {

    private static final Class<?> CraftOfflinePlayer_class = ReflectionUtils.getCraftbukkitClass("CraftOfflinePlayer");

    // Reflection cache
    private static Method m_CraftOfflinePlayer_getData;

    public static NBTTagCompoundNms getData_old(OfflinePlayer op) {
        try {
            Class<? extends OfflinePlayer> clazz = op.getClass();
            if (!CraftOfflinePlayer_class.isAssignableFrom(clazz)) throw new ClassCastException("class CraftOfflinePlayer is not assignable from " + clazz);
            if(m_CraftOfflinePlayer_getData == null) {
                m_CraftOfflinePlayer_getData = CraftOfflinePlayer_class.getDeclaredMethod("getData");
                m_CraftOfflinePlayer_getData.setAccessible(true);
            }
            Object data = m_CraftOfflinePlayer_getData.invoke(op);
            if(data == null) return null;
            return new NBTTagCompoundNms(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Nullable public static NBTTagCompoundNms getData(UUID uuid) {
        try {
            File worldFolder = Bukkit.getServer().getWorlds().get(0).getWorldFolder();
            File playerdata = new File(worldFolder, "playerdata");
            if(!playerdata.exists()) playerdata.mkdirs();
            File datfile = new File(playerdata, uuid + ".dat");
            if(!datfile.exists()) return null;
            return NBTTagCompoundNms.deserialize(new FileInputStream(datfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setData(UUID uuid, NBTTagCompoundNms nbt) {
        try {
            File worldFolder = Bukkit.getServer().getWorlds().get(0).getWorldFolder();
            File playerdata = new File(worldFolder, "playerdata");
            if(!playerdata.exists()) playerdata.mkdirs();
            File tmpfile = new File(playerdata, uuid + ".dat.tmp");
            File datfile = new File(playerdata, uuid + ".dat");
            nbt.serialize(new FileOutputStream(tmpfile));
            if (datfile.exists()) datfile.delete();
            tmpfile.renameTo(datfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
