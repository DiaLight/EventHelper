package dialight.nms;

import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class WorldNms8 extends WorldNms {

    public WorldNms8(org.bukkit.World world) {
        super(world);
    }

    public WorldServer asNms() {
        return ((CraftWorld) world).getHandle();
    }

    @Override public Object getHandle() {
        return asNms();
    }

}
