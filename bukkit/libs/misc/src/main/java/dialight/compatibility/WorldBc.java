package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class WorldBc {

    private static final Constructor<? extends WorldBc> constructor =
            ReflectionUtils.findCompatibleClass(WorldBc.class, World.class);

    protected final World world;

    public WorldBc(World world) {
        this.world = world;
    }

    public abstract Collection<Entity> getNearbyEntities(Location loc, double dx, double dy, double dz, Predicate<Entity> predicate);

    public static WorldBc of(World world) {
        return ReflectionUtils.newInstance(constructor, world);
    }

}
