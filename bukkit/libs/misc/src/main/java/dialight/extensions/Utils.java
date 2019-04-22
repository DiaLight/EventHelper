package dialight.extensions;

import dialight.exceptions.Todo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utils {


    public static boolean fastItemEquals(ItemStack st, ItemStack nd) {
        if(nd == null) return false;
        if(st.hashCode() != nd.hashCode()) return false;
        if(st.getType() != nd.getType()) return false;
        if(!st.getItemMeta().getDisplayName().equals(nd.getItemMeta().getDisplayName())) return false;
        if(st.getEnchantments().size() != nd.getEnchantments().size()) return false;
        if(st.getItemMeta().getLore().size() != nd.getItemMeta().getLore().size()) return false;
        final List<String>
                lst = st.getItemMeta().getLore(),
                lnd = nd.getItemMeta().getLore();
        for(int i = 0 ; i < st.getItemMeta().getLore().size() ; i++)
            if(!lst.get(i).equals(lnd.get(i))) return false;

        //return st.isSimilar(nd);
        return true;
    }

    public static void fastItemRemove(Inventory inv, ItemStack st) {
        for(int i = 0 ; i < inv.getContents().length ; i++)
            if(fastItemEquals(st, inv.getContents()[i])) inv.clear(i);
    }

    /**
     * Функция нахождения первого попавшегося блока
     * @param l Точка отсчета
     * @param v Шаг
     * @param i Количество шагов
     * @return Первый попавшийся блок
     */
    public static Location getTargetBlock(Location l, Vector v, int i) {
        Location r = l.clone();
        for (int j = 0; j < i; j++) {
            r.add(v);
            Block b = r.getBlock();
            if (b.getType() != Material.AIR) break;
        }
        return r;
    }

    /**
     * Ищет сущность по направлению куда смотрит player.
     * @param player Игрок от которого идет поиск.
     * @param distance Расстояние на котором ищутся сущности.
     * @param delta Ширина проверки.
     * @param types
     * @return Найденная сущность или null, если сущность не найдена.
     */
    @Nullable public static Entity getEnByDirection(Player player, double distance, double delta, EntityType... types) {
        Vector v = player.getLocation().getDirection();
        Location l = player.getEyeLocation().clone().add(v).add(v);
        double half = distance / 2;
        Location center = l.clone().add(v.clone().multiply(half));  // Центр отрезка
        Collection<Entity> entities = null;
        if(types.length == 0) {
            entities = player.getWorld().getNearbyEntities(center, half, half, half);
        } else {
            List<EntityType> etypes = Arrays.asList(types);
            entities = player.getWorld().getNearbyEntities(center, half, half, half, en -> {
                if(!etypes.contains(en.getType())) return false;
                return en.getLocation().distance(center) <= half;
            });
        }
        return getEnByDirection_impl(l, v, delta, entities);
    }
    @Nullable public static Entity getEnByDirection(Player player, double distance, double delta, Collection<Entity> entities) {
        Vector v = player.getLocation().getDirection();
        Location l = player.getEyeLocation().clone().add(v).add(v);
        return getEnByDirection(l, v, distance, delta, entities);
    }
    @Nullable public static Entity getEnByDirection(Location location, Vector direction, double distance, double delta, Collection<Entity> entities) {
        double half = distance / 2;
        Location center = location.clone().add(direction.clone().multiply(half));  // Центр отрезка
        Collection<Entity> filtered = new ArrayList<>();
        for(Entity en : entities) {
            if(en.getLocation().distance(center) <= half) filtered.add(en);
        }
        return getEnByDirection_impl(location, direction, delta, filtered);
    }
    @Nullable private static Entity getEnByDirection_impl(Location location, Vector direction, double delta, Collection<Entity> entities) {
        Entity closestEntity = null;
        double closestProj = delta;
        for (Entity en : entities) {
            Location t = en.getLocation().clone().add(0.0, 1.0, 0.0);
            double curProj = projectionToRay(location, direction, t);
            if (curProj <= closestProj) {
                closestProj = curProj;
                closestEntity = en;
            }  // Условие нахождения ближайшей к прямой сущности
        }
        return closestEntity;
    }

    /**
     * Находит расстояние от цели до прямой
     * @param location Точка на прямой
     * @param direction Вектор от точки задающий прямую
     * @param point Точка-цель
     * @return
     */
    public static double projectionToRay(Location location, Vector direction, Location point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double x0 = location.getX();
        double y0 = location.getY();
        double z0 = location.getZ();
        double vx = direction.getX();
        double vy = direction.getY();
        double vz = direction.getZ();
        double a = Math.pow((y0 - y) * vz - (z0 - z) * vy, 2.0) + Math.pow(
                (z0 - z) * vx - (x0 - x) * vz,
                2.0
        ) + Math.pow((x0 - x) * vy - (y0 - y) * vx, 2.0);
        double b = Math.pow(vx, 2.0) + Math.pow(vy, 2.0) + Math.pow(vz, 2.0);
        return Math.pow(a / b, 0.5);
    }

    public static int topDiv(int a, int b) {
        return (a + (b - 1)) / b;
    }

}
