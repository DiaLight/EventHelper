package dialight.compatibility;

import org.bukkit.Location;

public class LocationBc12 extends LocationBc {

    public LocationBc12(Location location) {
        super(location);
    }

    @Override public Location toBlockLocation() {
        return location.toBlockLocation();
    }

}
