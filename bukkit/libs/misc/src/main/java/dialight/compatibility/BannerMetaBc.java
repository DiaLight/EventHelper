package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.BannerMeta;

import java.lang.reflect.Constructor;

public abstract class BannerMetaBc {

    private static final Constructor<? extends BannerMetaBc> constructor =
            ReflectionUtils.findCompatibleConstructor(BannerMetaBc.class, BannerMeta.class);

    protected final BannerMeta meta;

    public BannerMetaBc(BannerMeta meta) {
        this.meta = meta;
    }

    public abstract void setBaseColor(DyeColor color);

    public static BannerMetaBc of(BannerMeta meta) {
        return ReflectionUtils.newInstance(constructor, meta);
    }

}
