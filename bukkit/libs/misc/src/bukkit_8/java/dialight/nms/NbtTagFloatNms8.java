package dialight.nms;

import net.minecraft.server.v1_8_R3.NBTTagFloat;

public class NbtTagFloatNms8 extends NbtTagFloatNms {

    public NbtTagFloatNms8(Object nbt) {
        super(nbt);
    }

    public static NbtTagFloatNms create(float val) {
        return new NbtTagFloatNms8(new NBTTagFloat(val));
    }

}
