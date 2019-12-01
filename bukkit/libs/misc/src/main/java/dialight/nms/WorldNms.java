package dialight.nms;

import org.bukkit.World;

import java.lang.reflect.Constructor;

public abstract class WorldNms {

    private static final Constructor<? extends WorldNms> constructor =
            ReflectionUtils.findCompatibleConstructor(WorldNms.class, World.class);

    protected final World world;

    public WorldNms(World world) {
        this.world = world;
    }

    public static WorldNms of(World world) {
        return ReflectionUtils.newInstance(constructor, world);
    }

    public abstract Object getHandle();

}
