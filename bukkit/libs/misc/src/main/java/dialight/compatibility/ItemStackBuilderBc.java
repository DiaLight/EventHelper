package dialight.compatibility;

import dialight.extensions.ItemStackBuilder;
import dialight.nms.ReflectionUtils;
import org.bukkit.DyeColor;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public abstract class ItemStackBuilderBc {

    private static final TreeMap<Integer, Constructor<? extends ItemStackBuilderBc>> backwardCompatibilityMap = new TreeMap<>();

    static {
        for (int minor = 8; minor < 32; minor++) {
            final String classPath = ItemStackBuilderBc.class.getName() + minor;
            try {
                Class<? extends ItemStackBuilderBc> act = (Class<? extends ItemStackBuilderBc>) Class.forName(classPath);
                final Constructor<? extends ItemStackBuilderBc> constructor = act.getConstructor(ItemStackBuilder.class);
                backwardCompatibilityMap.put(minor, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {}
        }
    }

    protected final ItemStackBuilder builder;

    public ItemStackBuilderBc(ItemStackBuilder builder) {
        this.builder = builder;
    }

    public abstract ItemStackBuilderBc stainedGlassPane(DyeColor color);

    public abstract ItemStackBuilderBc bed(DyeColor color);

    public abstract ItemStackBuilderBc playerHead();

    public static ItemStackBuilderBc of(ItemStackBuilder builder) {
        Map.Entry<Integer, Constructor<? extends ItemStackBuilderBc>> entry = backwardCompatibilityMap.floorEntry(ReflectionUtils.MINOR_VERSION);
        if(entry == null) throw new RuntimeException("Unsupported version");
        final Constructor<? extends ItemStackBuilderBc> constructor = entry.getValue();
        return ReflectionUtils.newInstance(constructor, builder);
    }

}
