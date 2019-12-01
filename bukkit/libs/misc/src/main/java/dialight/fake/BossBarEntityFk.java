package dialight.fake;

import dialight.nms.PlayerNms;
import dialight.nms.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Constructor;

public abstract class BossBarEntityFk {

    private static final Constructor<? extends BossBarEntityFk> constructor = ReflectionUtils.findCompatibleConstructor(BossBarEntityFk.class, World.class);

    private final World world;

    public BossBarEntityFk(World world) {
        this.world = world;
    }

    public abstract void sendSpawn(PlayerNms pbc, Location loc, String text, float healthPercent);

    public abstract void sendDespawn(PlayerNms pbc);
    public abstract void sendTeleport(PlayerNms pbc, Location loc);
    public abstract void sendUpdateText(PlayerNms pbc, String text);
    public abstract void sendUpdateHealth(PlayerNms pbc, float healthPercent);
    public abstract void sendUpdateBar(PlayerNms pbc, String text, float healthPercent);

    public static BossBarEntityFk create(World world) {
        return ReflectionUtils.newInstance(constructor, world);
    }

}
