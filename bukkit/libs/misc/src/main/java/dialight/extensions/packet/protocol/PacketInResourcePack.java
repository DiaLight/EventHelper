package dialight.extensions.packet.protocol;

import dialight.nms.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

public class PacketInResourcePack {

    public static final Class<?> PacketPlayInResourcePackStatus_class = ReflectionUtils.getNMSClass("PacketPlayInResourcePackStatus");

    private final Object packet;

    public PacketInResourcePack(Object packet) {
        this.packet = packet;
    }

    public Object getStatus() {
        String s = ReflectionUtils.getFieldValue(packet, "b").toString();
//        if (s.equals("DECLINED")) {
//
//        }
//        if (s.equals("FAILED_DOWNLOAD")) {
//
//        }
//        if (s.equals("ACCEPTED")) {
//
//        }
//        if (s.equals("SUCCESSFULLY_LOADED")) {
//            e.getPlayer().sendMessage("You have our texture pack installed");
//            return;
//        }
        return s;
    }

    @Nullable public static PacketInResourcePack of(Object packet) {
        Class<?> clazz = packet.getClass();
        if(clazz == PacketPlayInResourcePackStatus_class) {
            return new PacketInResourcePack(packet);
        }
        return null;
    }
}
