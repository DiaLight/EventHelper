package dialight.compatibility;

import org.bukkit.Location;

public class LocationBc12 extends LocationBc {

    public LocationBc12(Location location) {
        super(location);
    }

    @Override public Location toBlockLocation() {
        Location blockLoc = location.clone();
        blockLoc.setX(location.getBlockX());
        blockLoc.setY(location.getBlockY());
        blockLoc.setZ(location.getBlockZ());
        return blockLoc;
    }

}
