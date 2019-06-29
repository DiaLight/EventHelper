package dialight.compatibility;

import dialight.extensions.ItemStackBuilder;
import dialight.nms.ReflectionUtils;
import org.bukkit.DyeColor;

import java.lang.reflect.Constructor;

public abstract class ItemStackBuilderBc {

    private static final Constructor<? extends ItemStackBuilderBc> constructor =
            ReflectionUtils.findCompatibleClass(ItemStackBuilderBc.class, ItemStackBuilder.class);

    protected final ItemStackBuilder builder;

    public ItemStackBuilderBc(ItemStackBuilder builder) {
        this.builder = builder;
    }

    public abstract ItemStackBuilderBc wool(DyeColor color);

    public abstract ItemStackBuilderBc carpet(DyeColor color);

    public abstract ItemStackBuilderBc stainedGlass(DyeColor color);

    public abstract ItemStackBuilderBc stainedGlassPane(DyeColor color);

    public abstract ItemStackBuilderBc bed(DyeColor color);

    public abstract ItemStackBuilderBc banner(DyeColor color);

    public abstract ItemStackBuilderBc playerHead();

    public abstract ItemStackBuilderBc enderEye();

    public static ItemStackBuilderBc of(ItemStackBuilder builder) {
        return ReflectionUtils.newInstance(constructor, builder);
    }

}
