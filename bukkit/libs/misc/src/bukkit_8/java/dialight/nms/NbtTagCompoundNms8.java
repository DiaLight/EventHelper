package dialight.nms;

import net.minecraft.server.v1_8_R3.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NbtTagCompoundNms8 extends NbtTagCompoundNms {

    private final NBTTagCompound tag;

    public NbtTagCompoundNms8(Object nbt) {
        super(nbt);
        this.tag = (NBTTagCompound) nbt;
    }

    @Override public NbtTagCompoundNms merge(NbtTagCompoundNms nbt) {
        tag.a((NBTTagCompound) nbt.getNms());
//        if(ReflectionUtils.MINOR_VERSION >= 13) return NbtTagCompoundNms.of(merged);
        return this;
    }

    public boolean hasKey(String key) {
        return tag.hasKey(key);
    }
    public void set(String path, NbtBaseNms value) {
        tag.set(path, (NBTBase) value.getNms());
    }
    public Object getNms(String path) {
        return tag.get(path);
    }
    public void setByte(String path, byte value) {
        tag.setByte(path, value);
    }
    public void setShort(String path, short value) {
        tag.setShort(path, value);
    }
    public void setInt(String path, int value) {
        tag.setInt(path, value);
    }
    public void setLong(String path, long value) {
        tag.setLong(path, value);
    }
    public void setFloat(String path, float value) {
        tag.setFloat(path, value);
    }
    public void setDouble(String path, double value) {
        tag.setDouble(path, value);
    }
    public void setString(String path, String value) {
        tag.setString(path, value);
    }
    public void setByteArray(String path, byte[] value) {
        tag.setByteArray(path, value);
    }
    public void setIntArray(String path, int[] value) {
        tag.setIntArray(path, value);
    }
    public void setBoolean(String path, boolean value) {
        tag.setBoolean(path, value);
    }
    public byte getByte(String path) {
        return tag.getByte(path);
    }
    public short getShort(String path) {
        return tag.getShort(path);
    }
    public int getInt(String path) {
        return tag.getInt(path);
    }
    public long getLong(String path) {
        return tag.getLong(path);
    }
    public float getFloat(String path) {
        return tag.getFloat(path);
    }
    public double getDouble(String path) {
        return tag.getDouble(path);
    }
    public String getString(String path) {
        return tag.getString(path);
    }
    public byte[] getByteArray(String path) {
        return tag.getByteArray(path);
    }
    public int[] getIntArray(String path) {
        return tag.getIntArray(path);
    }
    public NbtTagCompoundNms getCompound(String path) {
        return NbtTagCompoundNms.of(tag.getCompound(path));
    }
    public NbtTagListNms getList(String path, Type type) {
        return NbtTagListNms.of(tag.getList(path, type.ordinal()));
    }
    public boolean getBoolean(String path) {
        return tag.getBoolean(path);
    }

    @Override public NbtTagCompoundNms clone() {
        return new NbtTagCompoundNms8(tag.clone());
    }

    public static NbtTagCompoundNms deserializeFromJson(String json) {
        try {
            return NbtTagCompoundNms.of(MojangsonParser.parse(json));
        } catch (MojangsonParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String serializeToJson() {
        return tag.toString();
    }

    public static NbtTagCompoundNms deserialize(InputStream is) throws IOException {
        return NbtTagCompoundNms.of(NBTCompressedStreamTools.a(is));
    }
    public void serialize(OutputStream os) throws IOException {
        NBTCompressedStreamTools.a(tag, os);
    }
    public static NbtTagCompoundNms create() {
        return new NbtTagCompoundNms8(new NBTTagCompound());
    }

}
