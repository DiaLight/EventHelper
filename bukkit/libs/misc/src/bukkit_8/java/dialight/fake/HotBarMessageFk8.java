package dialight.fake;

import dialight.nms.PlayerNms;
import dialight.nms.ReflectionUtils;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class HotBarMessageFk8 extends HotBarMessageFk {

    // These are the Class instances. Needed to get fields or methods for classes.
    private static Class<?> cl_PacketPlayOutChat;
    private static Class<?> cl_IChatBaseComponent;
    private static Class<?> cl_ChatMessage;
    private static Class<?> cl_ChatMessageType;

    // These are the constructors for those classes. Need to create new objects.
    private static Constructor<?> m_PacketPlayOutChat_init;
    private static Constructor<?> m_ChatMessage_init;

    // Used in 1.12+. Bytes are replaced with this enum
    private static Object v_ChatMessageType_ACTION_BAR;

    private static boolean byteMessageType = false;
    static {
        try {
            // This here sets the class fields.
            cl_PacketPlayOutChat = ReflectionUtils.getNMSClass("PacketPlayOutChat");
            cl_IChatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");
            try {
                cl_ChatMessageType = ReflectionUtils.getNMSClassOrNull("ChatMessageType");
                if(cl_ChatMessageType != null) {
                    v_ChatMessageType_ACTION_BAR = cl_ChatMessageType.getEnumConstants()[2];
                } else {
                    cl_ChatMessage = ChatMessageType.class;
                    v_ChatMessageType_ACTION_BAR = ChatMessageType.ACTION_BAR;
                }

                m_PacketPlayOutChat_init = cl_PacketPlayOutChat.getConstructor(cl_IChatBaseComponent, cl_ChatMessageType);
            } catch (NoSuchMethodException e) {
                m_PacketPlayOutChat_init = cl_PacketPlayOutChat.getConstructor(cl_IChatBaseComponent, byte.class);
                byteMessageType = true;
            }
            cl_ChatMessage = ReflectionUtils.getNMSClass("ChatMessage");
            m_ChatMessage_init = cl_ChatMessage.getConstructor(String.class, Object[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final PlayerNms playerNms;

    public HotBarMessageFk8(Player player) {
        super(player);
        playerNms = PlayerNms.of(player);
    }

    /**
     * Sends the hotbar message 'message' to the player 'player'
     */
    @Override public void sendMessage(String message) {
        try {
            // This creates the IChatComponentBase instance
            Object icb = m_ChatMessage_init.newInstance(message, new Object[0]);
            Object packet;
            if (byteMessageType) {
                packet = m_PacketPlayOutChat_init.newInstance(icb, (byte) 2);
            } else {
                packet = m_PacketPlayOutChat_init.newInstance(icb, v_ChatMessageType_ACTION_BAR);
            }
            playerNms.sendPacket(packet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
