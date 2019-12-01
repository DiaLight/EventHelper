package dialight.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public abstract class GameProfileNms {

    private static final Constructor<? extends GameProfileNms> constructor;
    private static Method m_create;

    static {
        Class<? extends GameProfileNms> clazz = ReflectionUtils.findCompatibleClass(GameProfileNms.class);
        try {
            constructor = clazz.getConstructor(Object.class);
            m_create = clazz.getDeclaredMethod("create", UUID.class, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object profile;

    public GameProfileNms(Object profile) {
        this.profile = profile;
    }

    public abstract Object getHandle();

    public static GameProfileNms of(Object nbt) {
        return ReflectionUtils.newInstance(constructor, nbt);
    }
    public static GameProfileNms create(UUID uuid, String name) {
        try {
            return (GameProfileNms) m_create.invoke(null, uuid, name);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String getName();

    public abstract UUID getUuid();
}
