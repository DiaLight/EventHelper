package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public abstract class LocationBc {

    private static final TreeMap<Integer, Constructor<? extends LocationBc>> backwardCompatibilityMap = new TreeMap<>();

    static {
        for (int minor = 8; minor < 32; minor++) {
            final String classPath = LocationBc.class.getName() + minor;
            try {
                Class<? extends LocationBc> act = (Class<? extends LocationBc>) Class.forName(classPath);
                final Constructor<? extends LocationBc> constructor = act.getConstructor(Location.class);
                backwardCompatibilityMap.put(minor, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {}
        }
    }

    protected final Location location;

    public LocationBc(Location location) {
        this.location = location;
    }

    public abstract Location toBlockLocation();

    public static LocationBc of(Location location) {
        Map.Entry<Integer, Constructor<? extends LocationBc>> entry = backwardCompatibilityMap.floorEntry(ReflectionUtils.MINOR_VERSION);
        if(entry == null) throw new RuntimeException("Unsupported version");
        final Constructor<? extends LocationBc> constructor = entry.getValue();
        return ReflectionUtils.newInstance(constructor, location);
    }
}
