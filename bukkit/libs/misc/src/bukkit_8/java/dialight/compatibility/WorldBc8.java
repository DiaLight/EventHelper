package dialight.compatibility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WorldBc8 extends WorldBc {

    public WorldBc8(World world) {
        super(world);
    }

    public Collection<Entity> getNearbyEntities(Location loc, double dx, double dy, double dz, Predicate<Entity> predicate) {
        return world.getNearbyEntities(loc, dx, dy, dz).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
