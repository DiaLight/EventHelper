package dialight.nms;

import java.lang.reflect.Method;

public class NbtTagListNmsU {  //  extends NbtTagListNms

    public static final Class<?> NBTBase_class;
    public static final Class<?> NBTCompressedStreamTools_class;
    public static final Class<?> MojangsonParser_class;
    public static final Class<?> NBTTagFloat_class;
    public static final Class<?> NBTTagDouble_class;

    private static Method m_NBTCompressedStreamTools_serializeNbt;
    private static Method m_NBTCompressedStreamTools_deserializeNbt;

    public static final Class<?> NBTTagCompound_class = ReflectionUtils.getNMSClass("NBTTagCompound");
    public static final Class<?> NBTTagList_class = ReflectionUtils.getNMSClass("NBTTagList");

    // Reflection cache
    private static Method m_NBTTagList_getFloat;
    private static Method m_NBTTagList_set;
    private static Method m_NBTTagList_getDouble;

    static {
        NBTBase_class = ReflectionUtils.getNMSClass("NBTBase");
        NBTCompressedStreamTools_class = ReflectionUtils.getNMSClass("NBTCompressedStreamTools");
        MojangsonParser_class = ReflectionUtils.getNMSClass("MojangsonParser");
        NBTTagFloat_class = ReflectionUtils.getNMSClass("NBTTagFloat");
        NBTTagDouble_class = ReflectionUtils.getNMSClass("NBTTagDouble");
    }

    protected final Object nbt;

    public NbtTagListNmsU(Object nbt) {
        if(nbt == null) throw new NullPointerException();
        this.nbt = nbt;
    }

    public Object set(int index, Object value) {
        if(m_NBTTagList_set == null) m_NBTTagList_set = ReflectionUtils.getMethod(NBTTagList_class, new String[]{"set", "a"}, int.class, NBTTagCompound_class);
        return ReflectionUtils.invokeMethod(m_NBTTagList_set, nbt, index, value);
    }

    public double getDouble(int index) {
        if(m_NBTTagList_getDouble == null) m_NBTTagList_getDouble = ReflectionUtils.findSingleMethod(NBTTagList_class, double.class, new String[]{"getDoubleAt"}, int.class);
        return (double) ReflectionUtils.invokeMethod(m_NBTTagList_getDouble, nbt, index);
    }

    public float getFloat(int index) {
        if(m_NBTTagList_getFloat == null) m_NBTTagList_getFloat = ReflectionUtils.findSingleMethod(NBTTagList_class, float.class, new String[]{"getFloatAt"}, int.class);
        return (float) ReflectionUtils.invokeMethod(m_NBTTagList_getFloat, nbt, index);
    }

    public boolean setDouble(int index, double value) {
        Object oldValue = set(index, NbtTagDoubleNms.create(value).getNms());
        return oldValue != null;
    }

    public boolean setFloat(int index, float value) {
        Object oldValue = set(index, NbtTagFloatNms.create(value).getNms());
        return oldValue != null;
    }

}
