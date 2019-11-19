package dialight.extensions;

import org.bukkit.Location;

public class LocationEx {

    private final Location location;

    public LocationEx(Location location) {
        this.location = location;
    }

    public Location keepRotation(Location to) {
        return new Location(
                to.getWorld(),
                to.getX(),
                to.getY(),
                to.getZ(),
                this.location.getYaw(),
                this.location.getPitch()
        );
    }

    public Location findSafeLoc() {
        Location loc = location.clone();
        while (loc.getBlock().getType().isSolid()) {
            loc.add(0, 1, 0);
        }
        return loc;
    }

    public static LocationEx of(Location location) {
        return new LocationEx(location);
    }

}
