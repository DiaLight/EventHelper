package dialight.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class NbtTagFloatNms implements NbtBaseNms {

    private static final Constructor<? extends NbtTagFloatNms> constructor;
    private static Method m_create;

    static {
        Class<? extends NbtTagFloatNms> clazz = ReflectionUtils.findCompatibleClass(NbtTagFloatNms.class);
        try {
            constructor = clazz.getConstructor(Object.class);
            m_create = clazz.getDeclaredMethod("create", float.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object nbt;

    public NbtTagFloatNms(Object nbt) {
        this.nbt = nbt;
    }

    public Object getNms() {
        return nbt;
    }

    public static NbtTagFloatNms of(Object nbt) {
        return ReflectionUtils.newInstance(constructor, nbt);
    }
    public static NbtTagFloatNms create(float val) {
        try {
            return (NbtTagFloatNms) m_create.invoke(null, val);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
