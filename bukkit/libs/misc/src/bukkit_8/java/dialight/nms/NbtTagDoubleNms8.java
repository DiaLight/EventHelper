package dialight.nms;

import net.minecraft.server.v1_8_R3.NBTTagDouble;

public class NbtTagDoubleNms8 extends NbtTagDoubleNms {

    public NbtTagDoubleNms8(Object nbt) {
        super(nbt);
    }

    public static NbtTagDoubleNms create(double val) {
        return new NbtTagDoubleNms8(new NBTTagDouble(val));
    }

}
