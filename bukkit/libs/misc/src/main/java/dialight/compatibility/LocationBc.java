package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.Location;

import java.lang.reflect.Constructor;

public abstract class LocationBc {

    private static final Constructor<? extends LocationBc> constructor =
            ReflectionUtils.findCompatibleConstructor(LocationBc.class, Location.class);

    protected final Location location;

    public LocationBc(Location location) {
        this.location = location;
    }

    public abstract Location toBlockLocation();

    public static LocationBc of(Location location) {
        return ReflectionUtils.newInstance(constructor, location);
    }

}
