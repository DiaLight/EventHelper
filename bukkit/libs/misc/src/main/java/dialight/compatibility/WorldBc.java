package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

public abstract class WorldBc {

    private static final TreeMap<Integer, Constructor<? extends WorldBc>> backwardCompatibilityMap = new TreeMap<>();

    static {
        for (int minor = 8; minor < 32; minor++) {
            final String classPath = WorldBc.class.getName() + minor;
            try {
                Class<? extends WorldBc> act = (Class<? extends WorldBc>) Class.forName(classPath);
                final Constructor<? extends WorldBc> constructor = act.getConstructor(World.class);
                backwardCompatibilityMap.put(minor, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {}
        }
    }

    protected final World world;

    public WorldBc(World world) {
        this.world = world;
    }

    public abstract Collection<Entity> getNearbyEntities(Location loc, double dx, double dy, double dz, Predicate<Entity> predicate);

    public static WorldBc of(World world) {
        Map.Entry<Integer, Constructor<? extends WorldBc>> entry = backwardCompatibilityMap.floorEntry(ReflectionUtils.MINOR_VERSION);
        if(entry == null) throw new RuntimeException("Unsupported version");
        final Constructor<? extends WorldBc> constructor = entry.getValue();
        return ReflectionUtils.newInstance(constructor, world);
    }

}
