package dialight.extensions;

import dialight.compatibility.WorldBc;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

    public Location cloneAsBlockOffset(double dx, double dy, double dz) {
        return new Location(
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        ).add(dx, dy, dz);
    }

    public void lookAt(Location loc) {
        lookAt(loc.toVector());
    }
    public void lookAt(Vector loc) {
        Vector vector = loc.clone().subtract(location.toVector());
        vector.normalize();
        location.setDirection(vector);
    }


    /**
     * Ищет сущность по направлению куда смотрит location.
     * @param distance Расстояние на котором ищутся сущности.
     * @param delta Ширина проверки.
     * @param types
     * @return Найденная сущность или null, если сущность не найдена.
     */
    @Nullable public Entity getEnByDirection(double distance, double delta, EntityType... types) {
        Vector v = location.getDirection();
        Location l = location.clone().add(v).add(v);
//        double half = distance / 2;
//        Location center = l.clone().add(v.clone().multiply(half));  // Центр отрезка
        Collection<Entity> entities = null;
//        if(types.length == 0) {
//            entities = location.getWorld().getNearbyEntities(center, half, half, half);
//        } else {
//            List<EntityType> etypes = Arrays.asList(types);
//            entities = WorldBc.of(location.getWorld()).getNearbyEntities(center, half, half, half, en -> {
//                if(!etypes.contains(en.getType())) return false;
//                return en.getLocation().distance(center) <= half;
//            });
//        }
        List<EntityType> etypes = Arrays.asList(types);
        entities = WorldBc.of(location.getWorld()).getNearbyEntities(l, distance, distance, distance, en -> {
            if(!etypes.contains(en.getType())) return false;
            return en.getLocation().distance(location) > 2;
        });
        return getObjByDirection_impl(l, v, 2, delta, entities, entity -> {
            return entity.getLocation().clone().add(0.0, 1.0, 0.0);
        });
    }
    @Nullable public <T> T getObjByDirection(double offs, double distance, double delta, Collection<T> objects, Function<T, Location> getLocation) {
        Vector v = location.getDirection();
        Location l = location.clone().add(v).add(v);
        double half = distance / 2;
        Location center = location.clone().add(v.clone().multiply(half));  // Центр отрезка
        Collection<T> filtered = new ArrayList<>();
        for(T obj : objects) {
            Location objLocation = getLocation.apply(obj);
            if(objLocation.distance(center) <= half) filtered.add(obj);
        }
        return getObjByDirection_impl(l, v, offs, delta, filtered, getLocation);
    }
    @Nullable public <T> T getObjByDirection(double offs, double delta, Collection<T> objects, Function<T, Location> getLocation) {
        Vector v = location.getDirection();
        Location l = location.clone().add(v);
        return getObjByDirection_impl(l, v, offs, delta, objects, getLocation);
    }
    @Nullable private <T> T getObjByDirection_impl(Location location, Vector direction, double offs, double delta, Collection<T> objects, Function<T, Location> getLocation) {
        T closestObj = null;
        direction = direction.normalize();
        Vector source = location.toVector();
        double closestProj = delta;
        for (T obj : objects) {
            Location target = getLocation.apply(obj);
            Vector relative = target.toVector().subtract(source);
            double distance = scalarProjection(relative, direction);  // relative projection on direction
            if(distance < offs) continue;
            double accuracy = projectionHeight(relative, direction);  // relative projection on direction
            if (accuracy > closestProj) continue;
            closestProj = accuracy;
            closestObj = obj;
        }
        return closestObj;
    }

    /**
     * Находит расстояние от текущего положения до проекции цели на прямую
     * @param a вектор от текущего положения до цели
     * @param nb Нормализованный вектор от текущего положения задающий прямую
     * @return длина проекции цели на прямую(расстояние поподания)
     */
    public static double scalarProjection(Vector a, Vector nb) {
        double ax = a.getX();
        double ay = a.getY();
        double az = a.getZ();

        double bx = nb.getX();
        double by = nb.getY();
        double bz = nb.getZ();

        // d = a * b  (dot product)  // used to get scalar projection
        double d = (ax * bx) + (ay * by) + (az * bz);

        // bl = |b|^2  (squared length)
//        double bl = Math.pow(bx, 2.0) + Math.pow(by, 2.0) + Math.pow(bz, 2.0);
        double bl = 1;  // b is normalized, so, it's length is 1

        return moduleSqrt(d / bl);  // projection length
    }

    public static double moduleSqrt(double val) {
        if(val >= 0) return Math.sqrt(val);
        return -Math.sqrt(-val);
    }

    /**
     * Находит расстояние от цели до прямой
     * @param a вектор от текущего положения до цели
     * @param nb Нормализованный вектор от текущего положения задающий прямую
     * @return расстояние от цели до ее проекции на прямую(точность попадания)
     */
    public static double projectionHeight(Vector a, Vector nb) {
        double ax = a.getX();
        double ay = a.getY();
        double az = a.getZ();

        double bx = nb.getX();
        double by = nb.getY();
        double bz = nb.getZ();

        // c = a x b  (cross product)  // length of cross product is area of vectors parallelogram. That from we get projection length
        double cx = ay * bz - az * by;
        double cy = az * bx - ax * bz;
        double cz = ax * by - ay * bx;

        // cl2 = |c|^2  (squared length)
        double cl2 = Math.pow(cx, 2.0) + Math.pow(cy, 2.0) + Math.pow(cz, 2.0);

        // bl = |b|^2  (squared length)
//        double bl = Math.pow(bx, 2.0) + Math.pow(by, 2.0) + Math.pow(bz, 2.0);
        double bl = 1;  // vector is normalized, so, it's length is 1

        return Math.sqrt(cl2 / bl);  // projection length
    }

    public static double projectionToRay_old(Location location, Vector direction, Location point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double x0 = location.getX();
        double y0 = location.getY();
        double z0 = location.getZ();
        double vx = direction.getX();
        double vy = direction.getY();
        double vz = direction.getZ();
        double a = Math.pow((y - y0) * vz - (z - z0) * vy, 2.0) + Math.pow(
                (z - z0) * vx - (x - x0) * vz,
                2.0
        ) + Math.pow((x - x0) * vy - (y - y0) * vx, 2.0);
        double b = Math.pow(vx, 2.0) + Math.pow(vy, 2.0) + Math.pow(vz, 2.0);
        return Math.pow(a / b, 0.5);
    }

}
