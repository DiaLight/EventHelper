package dialight.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class NbtTagDoubleNms implements NbtBaseNms {

    private static final Constructor<? extends NbtTagDoubleNms> constructor;
    private static Method m_create;

    static {
        Class<? extends NbtTagDoubleNms> clazz = ReflectionUtils.findCompatibleClass(NbtTagDoubleNms.class);
        try {
            constructor = clazz.getConstructor(Object.class);
            m_create = clazz.getDeclaredMethod("create", double.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object nbt;

    public NbtTagDoubleNms(Object nbt) {
        this.nbt = nbt;
    }

    public Object getNms() {
        return nbt;
    }

    public static NbtTagDoubleNms of(Object nbt) {
        return ReflectionUtils.newInstance(constructor, nbt);
    }
    public static NbtTagDoubleNms create(double val) {
        try {
            return (NbtTagDoubleNms) m_create.invoke(null, val);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
