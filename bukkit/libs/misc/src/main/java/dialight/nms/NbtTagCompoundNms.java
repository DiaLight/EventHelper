package dialight.nms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Жека не воруй
public abstract class NbtTagCompoundNms implements NbtBaseNms {

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

    private static final Constructor<? extends NbtTagCompoundNms> constructor;
    private static Method m_deserializeFromJson;
    private static Method m_deserialize;
    private static Method m_create;

    static {
        Class<? extends NbtTagCompoundNms> clazz = ReflectionUtils.findCompatibleClass(NbtTagCompoundNms.class);
        try {
            constructor = clazz.getConstructor(Object.class);
            m_deserializeFromJson = clazz.getDeclaredMethod("deserializeFromJson", String.class);
            m_deserialize = clazz.getDeclaredMethod("deserialize", InputStream.class);
            m_create = clazz.getDeclaredMethod("create");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object nbt;

    public NbtTagCompoundNms(Object nbt) {
        if(nbt == null) throw new NullPointerException();
        this.nbt = nbt;
    }

    public Object getNms() {
        return nbt;
    }

    public abstract NbtTagCompoundNms merge(NbtTagCompoundNms tag);

    public abstract boolean hasKey(String key);
    public abstract void set(String path, NbtBaseNms value);
    public abstract Object getNms(String path);
    public abstract void setByte(String path, byte value);
    public abstract void setShort(String path, short value);
    public abstract void setInt(String path, int value);
    public abstract void setLong(String path, long value);
    public abstract void setFloat(String path, float value);
    public abstract void setDouble(String path, double value);
    public abstract void setString(String path, String value);
    public abstract void setByteArray(String path, byte[] value);
    public abstract void setIntArray(String path, int[] value);
    public abstract void setBoolean(String path, boolean value);
    public abstract byte getByte(String path);
    public abstract short getShort(String path);
    public abstract int getInt(String path);
    public abstract long getLong(String path);
    public abstract float getFloat(String path);
    public abstract double getDouble(String path);
    public abstract String getString(String path);
    public abstract byte[] getByteArray(String path);
    public abstract int[] getIntArray(String path);
    public abstract NbtTagCompoundNms getCompound(String path);
    public abstract NbtTagListNms getList(String path, Type type);
    public abstract boolean getBoolean(String path);

    public abstract NbtTagCompoundNms clone();

    public static NbtTagCompoundNms deserializeFromJson(String json) {
        try {
            return (NbtTagCompoundNms) m_deserializeFromJson.invoke(null, json);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract String serializeToJson();

    public static NbtTagCompoundNms deserialize(InputStream is) throws IOException {
        try {
            return (NbtTagCompoundNms) m_deserialize.invoke(null, is);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if(e.getTargetException() instanceof IOException) throw (IOException) e.getTargetException();
            e.printStackTrace();
            return null;
        }
    }
    public abstract void serialize(OutputStream os) throws IOException;

    @Override public String toString() {
        return serializeToJson();
    }

    public static NbtTagCompoundNms of(Object nbt) {
        return ReflectionUtils.newInstance(constructor, nbt);
    }

    public static NbtTagCompoundNms create() {
        try {
            return (NbtTagCompoundNms) m_create.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
