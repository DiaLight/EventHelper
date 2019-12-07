package dialight.teams.captain.utils;

import dialight.misc.player.UuidPlayer;
import dialight.tuple.Tuple2t;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Schematic {

    private final Map<Vector, BlockData> blocks = new HashMap<>();
    private final Map<Vector, UuidPlayer> users = new HashMap<>();
    @Deprecated public Vector active;

    public void block(Vector pos, BlockData block) {
        blocks.put(pos, block);
    }

    public void user(Vector pos, UuidPlayer user) {
        users.put(pos, user);
    }

    public Tuple2t<Vector, Vector> getMinMax() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (Map.Entry<Vector, BlockData> entry : blocks.entrySet()) {
            Vector vec = entry.getKey();
            BlockData block = entry.getValue();
            if(vec.getX() < minX) minX = vec.getBlockX();
            if(vec.getX() > maxX) maxX = vec.getBlockX();
            if(vec.getY() < minY) minY = vec.getBlockY();
            if(vec.getY() > maxY) maxY = vec.getBlockY();
            if(vec.getZ() < minZ) minZ = vec.getBlockZ();
            if(vec.getZ() > maxZ) maxZ = vec.getBlockZ();
        }
        return new Tuple2t<>(new Vector(minX, minY, minZ), new Vector(maxX, maxY + 2, maxZ));
    }
    public Vector getSize() {
        Tuple2t<Vector, Vector> minMax = getMinMax();
        Vector min = minMax.getT1();
        Vector max = minMax.getT2();
        return max.subtract(min);
    }
    public Vector centerOffset() {
        Tuple2t<Vector, Vector> minMax = getMinMax();
        Vector min = minMax.getT1();
        Vector max = minMax.getT2();
        return min.multiply(-1);
    }

    public Map<Vector, BlockData> getBlocks() {
        return blocks;
    }

    public Map<Vector, UuidPlayer> getUsers() {
        return users;
    }
}
