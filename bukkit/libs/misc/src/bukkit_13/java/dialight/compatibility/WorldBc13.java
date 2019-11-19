package dialight.compatibility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.function.Predicate;

public class WorldBc13 extends WorldBc {

    public WorldBc13(World world) {
        super(world);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location loc, double dx, double dy, double dz, Predicate<Entity> predicate) {
        return world.getNearbyEntities(loc, dx, dy, dz, predicate);
    }

}
