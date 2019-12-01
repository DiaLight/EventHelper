package dialight.fake;

import dialight.nms.PlayerNms;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;


public class BossBarEntityFk8 extends BossBarEntityFk {

    private static Field f_PacketPlayOutSpawnEntityLiving_dataWatcher;
    private static int CUSTOM_ID;

    static {
        try {
            f_PacketPlayOutSpawnEntityLiving_dataWatcher = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("l");
            f_PacketPlayOutSpawnEntityLiving_dataWatcher.setAccessible(true);

            Field field = Entity.class.getDeclaredField("entityCount");
            field.setAccessible(true);
            CUSTOM_ID = field.getInt(null);
            field.set(null, CUSTOM_ID + 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final EntityLiving entity;
    private final float maxHealth;

    public BossBarEntityFk8(World world) {
        super(world);
        WorldServer craftWorld = ((CraftWorld) world).getHandle();
        this.entity = new EntityWither(craftWorld);
        this.maxHealth = entity.getMaxHealth();
    }

    @Override public void sendSpawn(PlayerNms pbc, Location loc , String text, float healthPercent) {
//        dragon = new EntityEnderDragon(world);
        entity.setLocation(
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                0,
                0
        );
//        dragon.setCustomName(text); //You can replace this with any character below 72 characters
//        dragon.setHealth(healthPercent);
//        dragon.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);

        DataWatcher watcher = new DataWatcher(null);
        // 1.8 protocol
        // https://wiki.vg/index.php?title=Protocol&oldid=7368#Spawn_Mob
        // https://wiki.vg/index.php?title=Entity_metadata&oldid=7360
        watcher.a(0, (byte) 0x20);  // flags: 0x01(On Fire) 0x02(Crouched) 0x08(Sprinting) 0x10(Eating/Drinking/Blocking) 0x20(Invisible)
        watcher.a(2, text);  // Name Tag
//        watcher.a(3, (byte) 1);  // Always show name tag
        float health = healthPercent * maxHealth;
        if(health < 1f) health = 1f;
        watcher.a(6, (float) health);  // Health
//        watcher.a(7, (int) 0);  // Potion effect color
//        watcher.a(8, (byte) 0);  // Is potion effect ambient
//        watcher.a(9, (byte) 0);  // Number of arrows in entity
//        watcher.a(10, text);
//        watcher.a(11, (byte) 1);
        watcher.a(20, (int) 881);

        try {
            f_PacketPlayOutSpawnEntityLiving_dataWatcher.set(packet, watcher);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        pbc.sendPacket(packet);
    }

    @Override public void sendDespawn(PlayerNms pbc) {
        pbc.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()));
    }

    @Override public void sendTeleport(PlayerNms pbc, Location loc) {
        pbc.sendPacket(new PacketPlayOutEntityTeleport(
                entity.getId(),
                (int) loc.getX() * 32,
                (int) loc.getY() * 32,
                (int) loc.getZ() * 32,
                (byte) ((int) loc.getYaw() * 256 / 360),
                (byte) ((int) loc.getPitch() * 256 / 360),
                false
        ));
    }

    @Override public void sendUpdateText(PlayerNms pbc, String text) {
        sendUpdateBar(pbc, text, -1);
    }

    @Override public void sendUpdateHealth(PlayerNms pbc, float healthPercent) {
        sendUpdateBar(pbc, null, healthPercent);
    }

    @Override public void sendUpdateBar(PlayerNms pbc, String text, float healthPercent) {
        DataWatcher watcher = new DataWatcher(null);
        watcher.a(0, (byte) 0x20);
        if (healthPercent != -1) {
            float health = healthPercent * maxHealth;
            if(health < 1f) health = 1f;
            watcher.a(6, (float) health);
        }
        if (text != null) {
//            watcher.a(10, text);
            watcher.a(2, text);
        }
//        watcher.a(11, (byte) 1);
        watcher.a(3, (byte) 1);

        pbc.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), watcher, true));
    }

}
