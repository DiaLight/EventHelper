package dialight.nms;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Жека не воруй
public class NBTTagCompoundNms {

    public enum Type {
        End,
        Byte,
        Short,
        Int,
        Long,
        Float,
        Double,
        Byte_Array,
        String,
        List,
        Compound,
        Int_Array,
        Long_Array
    }

    public static final Class<?> NMS_class = ReflectionUtils.getNMSClass("NBTTagCompound");
    public static final Class<?> NBTBase_class = ReflectionUtils.getNMSClass("NBTBase");
    public static final Class<?> NBTCompressedStreamTools_class = ReflectionUtils.getNMSClass("NBTCompressedStreamTools");
    public static final Class<?> MojangsonParser_class = ReflectionUtils.getNMSClass("MojangsonParser");
    public static final Class<?> NBTTagFloat_class = ReflectionUtils.getNMSClass("NBTTagFloat");
    public static final Class<?> NBTTagDouble_class = ReflectionUtils.getNMSClass("NBTTagDouble");

    // Reflection cache
    private static Method m_NBTCompressedStreamTools_serializeNbt;
    private static Method m_NBTCompressedStreamTools_deserializeNbt;
//    private static Method m_NBTTagCompound_merge;

    private final Object nbt;

    public NBTTagCompoundNms(Object nbt) {
        if(nbt == null) throw new NullPointerException();
        this.nbt = nbt;
    }

    public Object getInternal() {
        return nbt;
    }

    public NBTTagCompoundNms merge(NBTTagCompoundNms tag) {
//        if(m_NBTTagCompound_merge == null) m_NBTTagCompound_merge = ReflectionUtils.findSingleMethod(NMS_class, NMS_class, NMS_class);
//        ReflectionUtils.invokeMethod(m_NBTTagCompound_merge, nbt, tag.getInternal());
        Object merged = ReflectionUtils.invokeMethod(nbt, "a",
                new Class[]{NMS_class}, tag.getInternal());
        if(ReflectionUtils.MINOR_VERSION >= 13) return new NBTTagCompoundNms(merged);
        return this;
    }

    @Override public NBTTagCompoundNms clone() {
        Object cloned = ReflectionUtils.invokeMethod(nbt, "clone",
                new Class[]{});
        return new NBTTagCompoundNms(cloned);
    }

    public static Object createDouble(double value) {
        try {
            Constructor<?> constructor = NBTTagDouble_class.getConstructor(double.class);
            return constructor.newInstance(value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object createFloat(float value) {
        try {
            Constructor<?> constructor = NBTTagFloat_class.getConstructor(float.class);
            return constructor.newInstance(value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasKey(String key) {
        return (boolean) ReflectionUtils.invokeMethod(nbt, "hasKey",
                new Class[]{String.class}, key);
    }
    public void set(String path, Object value) {
        ReflectionUtils.invokeMethod(nbt, "set",
                new Class[]{String.class, NBTBase_class}, path, value);
    }

    public void setByte(String path, byte value) {
        ReflectionUtils.invokeMethod(nbt, "setByte",
                new Class[]{String.class, byte.class}, path, value);
    }

    public void setShort(String path, short value) {
        ReflectionUtils.invokeMethod(nbt, "setShort",
                new Class[]{String.class, short.class}, path, value);
    }

    public void setInt(String path, int value) {
        ReflectionUtils.invokeMethod(nbt, "setInt",
                new Class[]{String.class, int.class}, path, value);
    }

    public void setLong(String path, long value) {
        ReflectionUtils.invokeMethod(nbt, "setLong",
                new Class[]{String.class, long.class}, path, value);
    }

    public void setFloat(String path, float value) {
        ReflectionUtils.invokeMethod(nbt, "setFloat",
                new Class[]{String.class, float.class}, path, value);
    }

    public void setDouble(String path, double value) {
        ReflectionUtils.invokeMethod(nbt, "setDouble",
                new Class[]{String.class, double.class}, path, value);
    }

    public void setString(String path, String value) {
        ReflectionUtils.invokeMethod(nbt, "setString",
                new Class[]{String.class, String.class}, path, value);
    }

    public void setByteArray(String path, byte[] value) {
        ReflectionUtils.invokeMethod(nbt, "setByteArray",
                new Class[]{String.class, byte[].class}, path, value);
    }

    public void setIntArray(String path, int[] value) {
        ReflectionUtils.invokeMethod(nbt, "setIntArray",
                new Class[]{String.class, int[].class}, path, value);
    }

    public void setBoolean(String path, boolean value) {
        ReflectionUtils.invokeMethod(nbt, "setBoolean",
                new Class[]{String.class, boolean.class}, path, value);
    }

    public Object get(String path) {
        return ReflectionUtils.invokeMethod(nbt, "get",
                        new Class[]{String.class}, path);
    }

    public byte getByte(String path) {
        return (byte) ReflectionUtils.invokeMethod(nbt, "getByte",
                new Class[]{String.class}, path);
    }
    public short getShort(String path) {
        return (short) ReflectionUtils.invokeMethod(nbt, "getShort",
                new Class[]{String.class}, path);
    }
    public int getInt(String path) {
        return (int) ReflectionUtils.invokeMethod(nbt, "getInt",
                new Class[]{String.class}, path);
    }
    public long getLong(String path) {
        return (long) ReflectionUtils.invokeMethod(nbt, "getLong",
                new Class[]{String.class}, path);
    }
    public float getFloat(String path) {
        return (float) ReflectionUtils.invokeMethod(nbt, "getFloat",
                new Class[]{String.class}, path);
    }
    public double getDouble(String path) {
        return (double) ReflectionUtils.invokeMethod(nbt, "getDouble",
                new Class[]{String.class}, path);
    }
    public String getString(String path) {
        return (String) ReflectionUtils.invokeMethod(nbt, "getString",
                new Class[]{String.class}, path);
    }
    public byte[] getByteArray(String path) {
        return (byte[]) ReflectionUtils.invokeMethod(nbt, "getByteArray",
                new Class[]{String.class}, path);
    }
    public int[] getIntArray(String path) {
        return (int[]) ReflectionUtils.invokeMethod(nbt, "getIntArray",
                new Class[]{String.class}, path);
    }
    public NBTTagCompoundNms getCompound(String path) {
        Object nbt = ReflectionUtils.invokeMethod(this.nbt, "getCompound",
                new Class[]{String.class}, path);
        if(nbt == null) return null;
        return new NBTTagCompoundNms(nbt);
    }
    @Nullable public NBTTagListNms getList(String path, Type type) {
        return new NBTTagListNms(ReflectionUtils.invokeMethod(nbt, "getList",
                new Class[]{String.class, int.class}, path, type.ordinal()));
    }
    public boolean getBoolean(String path) {
        return (boolean) ReflectionUtils.invokeMethod(nbt, "getBoolean",
                new Class[]{String.class}, path);
    }

    public static NBTTagCompoundNms deserializeFromString(String json) {
        return new NBTTagCompoundNms(ReflectionUtils.invokeMethod(MojangsonParser_class, null, "parse",
                new Class[]{String.class}, json));
    }

    public String serializeToString() {
        return nbt.toString();
    }

    public static NBTTagCompoundNms deserialize(InputStream is) {
        if(m_NBTCompressedStreamTools_deserializeNbt == null) m_NBTCompressedStreamTools_deserializeNbt = ReflectionUtils.findSingleMethod(
                NBTTagCompoundNms.NBTCompressedStreamTools_class, NBTTagCompoundNms.NMS_class, InputStream.class
        );
        try {
            Object nbt = m_NBTCompressedStreamTools_deserializeNbt.invoke(null, is);
            return new NBTTagCompoundNms(nbt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public void serialize(OutputStream os) {
        if(m_NBTCompressedStreamTools_serializeNbt == null) m_NBTCompressedStreamTools_serializeNbt = ReflectionUtils.findSingleMethod(
                NBTTagCompoundNms.NBTCompressedStreamTools_class, void.class, NBTTagCompoundNms.NMS_class, OutputStream.class
        );
        ReflectionUtils.invokeMethod(m_NBTCompressedStreamTools_serializeNbt, null, nbt, os);
    }

    @Override
    public String toString() {
        return serializeToString();
    }

}
