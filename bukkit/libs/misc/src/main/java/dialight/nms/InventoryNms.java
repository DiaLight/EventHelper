package dialight.nms;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;

public class InventoryNms {

    // Reflection cache
    private static Method m_Player_GetHandle;
    private static Method m_PlayerConnection_sendPacket;
    private static Method m_CraftChatMessage_fromString;
    private static Method m_EntityPlayer_updateInventory;
    private static Field f_EntityPlayer_playerConnection;
    private static Field f_EntityPlayer_activeContainer;
    private static Field f_Container_windowId;
    private static Constructor<?> c_PacketOpenWindow;
    private static Class<?> Containers_class;

    public static void sendInventoryTitle(@NotNull Player player, Inventory inventory, String title) {
        try {
            if (m_Player_GetHandle == null) {
                m_Player_GetHandle = player.getClass().getMethod("getHandle");
            }
            Object nms_EntityPlayer = m_Player_GetHandle.invoke(player);
            if (f_EntityPlayer_playerConnection == null) {
                f_EntityPlayer_playerConnection = nms_EntityPlayer.getClass().getField("playerConnection");
            }
            Object nms_PlayerConnection = f_EntityPlayer_playerConnection.get(nms_EntityPlayer);
            if (f_EntityPlayer_activeContainer == null) {
                f_EntityPlayer_activeContainer = nms_EntityPlayer.getClass().getField("activeContainer");
            }
            Object nms_Container = f_EntityPlayer_activeContainer.get(nms_EntityPlayer);
            if (f_Container_windowId == null) {
                f_Container_windowId = nms_Container.getClass().getField("windowId");
            }
            int windowId = f_Container_windowId.getInt(nms_Container);
            if (ReflectionUtils.MINOR_VERSION <= 6) {  // 1.5.X - 1.6.X
                sendPacket5a6a7(nms_PlayerConnection, nms_EntityPlayer, nms_Container, windowId, inventory, title, false);
            } else if (ReflectionUtils.MINOR_VERSION <= 7) {  // 1.7.X
                sendPacket5a6a7(nms_PlayerConnection, nms_EntityPlayer, nms_Container, windowId, inventory, title, true);
            } else if (ReflectionUtils.MINOR_VERSION <= 13) {  // 1.8.X - 1.13.X
                sendPacket8(nms_PlayerConnection, nms_EntityPlayer, nms_Container, windowId, inventory, title);
            } else {  // 1.14 - ...
                sendPacket14(nms_PlayerConnection, nms_EntityPlayer, nms_Container, windowId, inventory, title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket5a6a7(Object nms_playerConnection, Object nms_EntityPlayer, Object nms_Container, int windowId, Inventory inventory, String title, boolean flag) throws Exception {
        if (c_PacketOpenWindow == null) {
            if (flag) {
                Class<?> PacketPlayOutOpenWindow_class = ReflectionUtils.getNMSClass("PacketPlayOutOpenWindow");
                c_PacketOpenWindow = PacketPlayOutOpenWindow_class.getConstructor(int.class, int.class, String.class, int.class, boolean.class);
            } else {
                Class<?> Packet100OpenWindow_class = ReflectionUtils.getNMSClass("Packet100OpenWindow");
                c_PacketOpenWindow = Packet100OpenWindow_class.getConstructor(int.class, int.class, String.class, int.class, boolean.class);
            }
        }

        int id;
        int size;

        switch (inventory.getType()) {
            case ANVIL:
                id = 8;
                size = 9;
                break;
            case BEACON:
                id = 7;
                size = 1;
                break;
            case BREWING:
                id = 5;
                size = 4;
                break;
            case CRAFTING:
                return;
            case CREATIVE:
                return;
            case DISPENSER:
                id = 3;
                size = 9;
                break;
            case DROPPER:
                id = 10;
                size = 9;
                break;
            case ENCHANTING:
                id = 4;
                size = 9;
                break;
            case ENDER_CHEST:
            case CHEST:
                id = 0;
                size = inventory.getSize();
                break;
            case FURNACE:
                id = 2;
                size = 2;
                break;
            case HOPPER:
                id = 9;
                size = 5;
                break;
            case MERCHANT:
                id = 6;
                size = 3;
                break;
            case PLAYER:
                return;
            case WORKBENCH:
                id = 1;
                size = 9;
                break;
            default:
                return;
        }

        if (title != null && title.length() > 32) {
            title = title.substring(0, 32);
        }

        if (m_EntityPlayer_updateInventory == null) {
            Class<?> Container_class = ReflectionUtils.getNMSClass("Container");
            m_EntityPlayer_updateInventory = nms_EntityPlayer.getClass().getMethod("updateInventory", Container_class);
        }

        Object packet = c_PacketOpenWindow.newInstance(windowId, id, title != null ? title : "", size, true);
        sendPacket(nms_playerConnection, packet);

        m_EntityPlayer_updateInventory.invoke(nms_EntityPlayer, nms_Container);
    }

    private static void sendPacket8(Object nms_playerConnection, Object nms_EntityPlayer, Object nms_Container, int windowId, Inventory inventory, String title) throws Exception {
        if (c_PacketOpenWindow == null) {
            Class<?> IChatBaseComponent_class = ReflectionUtils.getNMSClass("IChatBaseComponent");
            Class<?> PacketPlayOutOpenWindow_class = ReflectionUtils.getNMSClass("PacketPlayOutOpenWindow");
            c_PacketOpenWindow = PacketPlayOutOpenWindow_class.getConstructor(int.class, String.class, IChatBaseComponent_class, int.class);
        }

        String id;
        int size = 0;

        switch (inventory.getType()) {
            case ANVIL:
                id = "minecraft:anvil";
                break;
            case BEACON:
                id = "minecraft:beacon";
                break;
            case BREWING:
                id = "minecraft:brewing_stand";
                break;
            case CRAFTING:
                return;
            case CREATIVE:
                return;
            case DISPENSER:
                id = "minecraft:dispenser";
                break;
            case DROPPER:
                id = "minecraft:dropper";
                break;
            case ENCHANTING:
                id = "minecraft:enchanting_table";
                break;
            case ENDER_CHEST:
            case CHEST:
                id = "minecraft:chest";
                size = inventory.getSize();
                break;
            case FURNACE:
                id = "minecraft:furnace";
                break;
            case HOPPER:
                id = "minecraft:hopper";
                break;
            case MERCHANT:
                id = "minecraft:villager";
                size = 3;
                break;
            case PLAYER:
                return;
            case WORKBENCH:
                id = "minecraft:crafting_table";
                break;
            default:
                return;
        }

        if (m_CraftChatMessage_fromString == null) {
            Class<?> craftChatMessage_class = ReflectionUtils.getCraftbukkitClass("util.CraftChatMessage");
            m_CraftChatMessage_fromString = craftChatMessage_class.getMethod("fromString", String.class);
        }
        if (m_EntityPlayer_updateInventory == null) {
            Class<?> Container_class = ReflectionUtils.getNMSClass("Container");
            m_EntityPlayer_updateInventory = nms_EntityPlayer.getClass().getMethod("updateInventory", Container_class);
        }

        Object nms_title = ((Object[]) m_CraftChatMessage_fromString.invoke(null, title))[0];
        Object nms_packet = c_PacketOpenWindow.newInstance(windowId, id, nms_title, size);
        sendPacket(nms_playerConnection, nms_packet);

        m_EntityPlayer_updateInventory.invoke(nms_EntityPlayer, nms_Container);
    }

    private static final Map<InventoryType, Object> containerCache = new EnumMap<>(InventoryType.class);
    @Nullable private static Object getContainer14(InventoryType type) {
        Object container = containerCache.get(type);
        if(container != null) return container;
        switch (type) {
            case ANVIL:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "ANVIL");
                break;
            case BEACON:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "BEACON");
                break;
            case BREWING:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "BREWING_STAND");
                break;
            case CRAFTING:
                return null;
            case CREATIVE:
                return null;
            case DISPENSER:
            case DROPPER:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "GENERIC_3X3");
                break;
            case ENCHANTING:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "ENCHANTMENT");
                break;
            case ENDER_CHEST:
            case CHEST:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "GENERIC_9X6");
                break;
            case FURNACE:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "FURNACE");
                break;
            case HOPPER:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "HOPPER");
                break;
            case MERCHANT:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "MERCHANT");
                break;
            case PLAYER:
                return null;
            case WORKBENCH:
                container = ReflectionUtils.getFieldValue(Containers_class, null, "CRAFTING");
                break;
            default:
                return null;
        }
        containerCache.put(type, container);
        return container;
    }
    private static void sendPacket14(Object nms_playerConnection, Object nms_EntityPlayer, Object nms_Container, int windowId, Inventory inventory, String title) throws Exception {
        if(Containers_class == null) Containers_class = ReflectionUtils.getNMSClass("Containers");
        if (c_PacketOpenWindow == null) {
            Class<?> IChatBaseComponent_class = ReflectionUtils.getNMSClass("IChatBaseComponent");
            Class<?> PacketPlayOutOpenWindow_class = ReflectionUtils.getNMSClass("PacketPlayOutOpenWindow");
            c_PacketOpenWindow = PacketPlayOutOpenWindow_class.getConstructor(int.class, Containers_class, IChatBaseComponent_class);
        }

        Object container = getContainer14(inventory.getType());
        if(container == null) return;

        if (m_CraftChatMessage_fromString == null) {
            Class<?> craftChatMessage_class = ReflectionUtils.getCraftbukkitClass("util.CraftChatMessage");
            m_CraftChatMessage_fromString = craftChatMessage_class.getMethod("fromString", String.class);
        }
        if (m_EntityPlayer_updateInventory == null) {
            Class<?> Container_class = ReflectionUtils.getNMSClass("Container");
            m_EntityPlayer_updateInventory = nms_EntityPlayer.getClass().getMethod("updateInventory", Container_class);
        }

        Object nms_title = ((Object[]) m_CraftChatMessage_fromString.invoke(null, title))[0];
        Object nms_packet = c_PacketOpenWindow.newInstance(windowId, container, nms_title);
        sendPacket(nms_playerConnection, nms_packet);

        m_EntityPlayer_updateInventory.invoke(nms_EntityPlayer, nms_Container);
    }

    private static void sendPacket(Object playerConnection, Object packet) throws Exception {
        if (m_PlayerConnection_sendPacket == null) {
            Class<?> Packet_class = ReflectionUtils.getNMSClass("Packet");
            m_PlayerConnection_sendPacket = playerConnection.getClass().getMethod("sendPacket", Packet_class);
        }
        m_PlayerConnection_sendPacket.invoke(playerConnection, packet);
    }
}
