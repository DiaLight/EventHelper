package dialight.nms;

import dialight.extensions.packet.ChannelPacketHandler;
import dialight.extensions.packet.PacketHandler;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class PlayerNms {

    private static Class<?> EntityPlayer_class;
    private static Class<?> NetworkManager_class;
    private static Class<?> PlayerConnection_class;
    private static Field f_EntityPlayer_playerConnection;
    private static Field f_PlayerConnection_networkManager;
    private static Field f_NetworkManager_channel;

    static {
        EntityPlayer_class = ReflectionUtils.getNMSClass("EntityPlayer");
        f_EntityPlayer_playerConnection = ReflectionUtils.getField(EntityPlayer_class, "playerConnection");

        PlayerConnection_class = ReflectionUtils.getNMSClass("PlayerConnection");
        f_PlayerConnection_networkManager = ReflectionUtils.getField(PlayerConnection_class, "networkManager");

        NetworkManager_class = ReflectionUtils.getNMSClass("NetworkManager");
        f_NetworkManager_channel = ReflectionUtils.getField(NetworkManager_class, "channel");
    }

    private final Player player;

    public PlayerNms(Player player) {
        this.player = player;
    }

    public Object getHandle() {
        return ReflectionUtils.invokeMethod(player, "getHandle", new Class[0]);
    }

    @Nullable public PacketHandler getPacketHandler(String name) {
        return (PacketHandler) getChannel().pipeline().get(name);
    }

    @NotNull public PacketHandler getOrCreatePacketHandler(String name) {
        Channel ch = getChannel();
        ChannelPacketHandler handler = (ChannelPacketHandler) ch.pipeline().get(name);
        if(handler == null) {
            handler = new ChannelPacketHandler(player, name);
            ch.pipeline().addBefore("packet_handler", name, handler);
        }
        return handler;
    }

    public void removePacketHandler(String name) {
        Channel ch = getChannel();
        try {
            ch.pipeline().remove(name);
        } catch (NoSuchElementException ignored) {
//            System.out.println("can't remove " + name);
//            System.out.println(ch.pipeline().names());
        }
    }

    private Object getPlayerConnection() {
        return ReflectionUtils.getFieldValue(f_EntityPlayer_playerConnection, getHandle());
    }

    private Object getNetworkManager() {
        return ReflectionUtils.getFieldValue(f_PlayerConnection_networkManager, getPlayerConnection());
    }

    private Channel getChannel() {
        return (Channel) ReflectionUtils.getFieldValue(f_NetworkManager_channel, getNetworkManager());
    }

    public static PlayerNms of(Player player) {
        return new PlayerNms(player);
//        return ReflectionUtils.newInstance(constructor, player);
    }

}