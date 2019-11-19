package dialight.compatibility;

import org.bukkit.Location;

public class LocationBc8 extends LocationBc {

    public LocationBc8(Location location) {
        super(location);
    }

    @Override
    public Location toBlockLocation() {
        return new Location(
                location.getWorld(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                location.getYaw(), location.getPitch()
        );
    }

}
