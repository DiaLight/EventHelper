package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.DyeColor;

import java.lang.reflect.Constructor;

public abstract class DyeColorBc {

    private static final Constructor<? extends DyeColorBc> constructor =
            ReflectionUtils.findCompatibleClass(DyeColorBc.class);

    public static final DyeColor LIGHT_GRAY = of().lightGray();

    public abstract DyeColor lightGray();

    public static DyeColorBc of() {
        return ReflectionUtils.newInstance(constructor);
    }

}
