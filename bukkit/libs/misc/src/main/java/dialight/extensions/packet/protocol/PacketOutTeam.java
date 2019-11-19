package dialight.extensions.packet.protocol;

import dialight.nms.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;

public class PacketOutTeam {

    public static final Class<?> PacketPlayOutScoreboardTeam_class = ReflectionUtils.getNMSClass("PacketPlayOutScoreboardTeam");
    public static final Field f_PacketPlayOutScoreboardTeam_name;
    public static final Field f_PacketPlayOutScoreboardTeam_members;
    public static final Field f_PacketPlayOutScoreboardTeam_mode;

    static {
        f_PacketPlayOutScoreboardTeam_name = ReflectionUtils.getField(PacketPlayOutScoreboardTeam_class, "a");
        f_PacketPlayOutScoreboardTeam_members = ReflectionUtils.findFirstDeclaredField(PacketPlayOutScoreboardTeam_class, Collection.class);
        if(ReflectionUtils.MINOR_VERSION <= 8) {
            f_PacketPlayOutScoreboardTeam_mode = ReflectionUtils.getField(PacketPlayOutScoreboardTeam_class, "h");
        } else {
            f_PacketPlayOutScoreboardTeam_mode = ReflectionUtils.getField(PacketPlayOutScoreboardTeam_class, "i");
        }
    }

    private final Object packet;

    public PacketOutTeam(Object packet) {
        this.packet = packet;
    }

    public String getName() {
        return (String) ReflectionUtils.getFieldValue(f_PacketPlayOutScoreboardTeam_name, packet);
    }

    private int getMode() {
        return (int) ReflectionUtils.getFieldValue(f_PacketPlayOutScoreboardTeam_mode, packet);
    }

    public boolean isCreate() {
        return getMode() == 0;
    }

    public boolean isRemove() {
        return getMode() == 1;
    }

    public boolean isUpdate() {
        return getMode() == 2;
    }

    public boolean isAddPlayer() {
        return getMode() == 3;
    }

    public boolean isRemovePlayer() {
        return getMode() == 4;
    }

    public Collection<String> getMembers() {
        return (Collection<String>) ReflectionUtils.getFieldValue(f_PacketPlayOutScoreboardTeam_members, packet);
    }

    @Nullable
    public static PacketOutTeam of(Object packet) {
        Class<?> clazz = packet.getClass();
        if(clazz == PacketPlayOutScoreboardTeam_class) return new PacketOutTeam(packet);
        return null;
    }

    @Override
    public String toString() {
        if(isCreate()) {
            return "PacketOutTeam{create " + getName() + '}';
        }
        if(isRemove()) {
            return "PacketOutTeam{remove " + getName() + '}';
        }
        if(isUpdate()) {
            return "PacketOutTeam{update " + getName() + '}';
        }
        if(isAddPlayer()) {
            return "PacketOutTeam{add_members " + getMembers() + '}';
        }
        if(isRemovePlayer()) {
            return "PacketOutTeam{remove_members " + getMembers() + '}';
        }
        return "PacketOutTeam{unknown}";
    }
}
