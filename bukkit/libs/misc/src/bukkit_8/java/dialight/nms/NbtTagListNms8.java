package dialight.nms;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class NbtTagListNms8 extends NbtTagListNms {

    private final NBTTagList tag;

    public NbtTagListNms8(Object nbt) {
        super(nbt);
        this.tag = (NBTTagList) nbt;
    }

    public int getType() {
        return tag.f();
    }

    @Override public int size() {
        return tag.size();
    }

    @Override public NbtTagCompoundNms getCompound(int i) {
        return NbtTagCompoundNms.of(tag.get(i));
    }

    @Override public Object set(int index, NbtBaseNms value) {
        NBTBase oldValue = this.tag.a(index);
        this.tag.a(index, (NBTBase) value.getNms());
        return oldValue;
    }
    @Override public boolean add(NbtBaseNms value) {
        this.tag.add((NBTBase) value.getNms());
        return true;
    }
    public double getDouble(int index) {
        return tag.d(index);
    }
    public float getFloat(int index) {
        return tag.e(index);
    }
    public boolean setDouble(int index, double value) {
        if (index < 0 || index >= this.size()) return false;
        tag.a(index, (NBTBase) NbtTagDoubleNms.create(value).getNms());
        return true;
    }
    public boolean setFloat(int index, float value) {
        if (index < 0 || index >= this.size()) return false;
        tag.a(index, (NBTBase) NbtTagFloatNms.create(value).getNms());
        return true;
    }

    public static NbtTagListNms create() {
        return new NbtTagListNms8(new NBTTagList());
    }

}
