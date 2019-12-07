package dialight.misc.player;

import com.google.common.base.Charsets;
import dialight.extensions.ServerEx;
import dialight.extensions.UuidEx;
import dialight.misc.Hex;
import dialight.nms.GameProfileNms;
import dialight.nms.NbtTagCompoundNms;
import dialight.nms.OfflinePlayerNms;
import dialight.nms.PlayerNms;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEngine implements Listener {

    private final Plugin plugin;
    private final Map<UUID, PlayerEntry> playersMap = new HashMap<>();
    private final Map<String, UUID> cachedUuid = new HashMap<>();

    public PlayerEngine(Plugin plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(this, plugin);
        for (OfflinePlayer op : plugin.getServer().getOfflinePlayers()) {
            UUID offlineUuid = generateOfflineUuid(op.getName());
            if(plugin.getServer().getOnlineMode()) {
                if(offlineUuid.equals(op.getUniqueId())) continue;
                cachedUuid.put(op.getName(), op.getUniqueId());
            } else {
                cachedUuid.put(op.getName(), offlineUuid);
            }
        }
    }

    public void disable() {
        World mainWorld = ServerEx.of(plugin.getServer()).getMainWorld();
        for (Map.Entry<UUID, PlayerEntry> entry : playersMap.entrySet()) {
            OfflinePlayerNms.setData(mainWorld, entry.getKey(), entry.getValue().nbtp.getNbt());
        }
        HandlerList.unregisterAll(this);
    }
    @NotNull public UuidPlayer getOrLoad(UUID uuid) {
        PlayerEntry entry = playersMap.get(uuid);
        if(entry != null) return entry.uidp;

        World mainWorld = ServerEx.of(plugin.getServer()).getMainWorld();
        NbtTagCompoundNms nbt = OfflinePlayerNms.getData(mainWorld, uuid);

        NbtPlayer nbtp = new NbtPlayer(plugin.getServer());
        String name = null;
        if(nbt == null) {
            name = tryResolveName(plugin.getServer(), uuid);

            nbt = PlayerNms.createNbt(mainWorld, GameProfileNms.create(uuid, name));
            NbtTagCompoundNms bukkitNbt = NbtTagCompoundNms.create();
            bukkitNbt.setString("lastKnownName", name);
            nbt.set("bukkit", bukkitNbt);

            OfflinePlayerNms.setData(mainWorld, uuid, nbt);
            nbtp.setNbt(nbt);
        } else {
            nbtp.setNbt(nbt);
            name = tryResolveName(plugin.getServer(), uuid);
            if(name == null) name = nbtp.getName();
            if(name != null) {
                UUID offlineUuid = generateOfflineUuid(name);
                if(plugin.getServer().getOnlineMode()) {
                    if(!uuid.equals(offlineUuid)) {
                        cachedUuid.put(name, uuid);
                    }
                } else {
                    if(uuid.equals(offlineUuid)) {
                        cachedUuid.put(name, uuid);
                    }
                }
            }
        }

        UuidPlayer uidp = new UuidPlayer(plugin.getServer(), uuid, name, nbtp);
        entry = new PlayerEntry(nbtp, uidp);
        playersMap.put(uuid, entry);
        return entry.uidp;
    }

    private String tryResolveName(Server server, UUID uuid) {
        String name = server.getOfflinePlayer(uuid).getName();
        if(name != null) return name;
        Player player = server.getPlayer(uuid);
        if(player != null) return player.getName();
        return PlayerEngine.resolveName(uuid);
    }

    public UuidPlayer getOrLoad(String name) {
        UUID uuid = cachedUuid.get(name);
        if(uuid != null) return getOrLoad(uuid);
        if(plugin.getServer().getOnlineMode()) uuid = resolveUuid(name);
        if(uuid == null) uuid = generateOfflineUuid(name);
        cachedUuid.put(name, uuid);
        return getOrLoad(uuid);
    }

    @EventHandler public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            PlayerEntry entry = playersMap.get(uuid);
            if (entry != null) {
                World mainWorld = ServerEx.of(plugin.getServer()).getMainWorld();
                OfflinePlayerNms.setData(mainWorld, uuid, entry.nbtp.getNbt());
            }
        });
    }

    @EventHandler public void onQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            PlayerEntry entry = playersMap.get(uuid);
            if(entry != null) {
                World mainWorld = ServerEx.of(plugin.getServer()).getMainWorld();
                NbtTagCompoundNms nbt = OfflinePlayerNms.getData(mainWorld, uuid);
                if(nbt == null) throw new NullPointerException("can't find player nbt file for " + uuid);
                entry.nbtp.setNbt(nbt);
            }

        });
    }

    private static class PlayerEntry {

        public final NbtPlayer nbtp;
        public final UuidPlayer uidp;

        private PlayerEntry(NbtPlayer nbtp, UuidPlayer uidp) {
            this.nbtp = nbtp;
            this.uidp = uidp;
        }
    }

    public static UUID generateOfflineUuid(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
    }

    @Nullable public static UUID resolveUuid(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            StringBuilder sb = new StringBuilder();
            try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                    sb.append("\n");
                }
            }
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(sb.toString());
            String uuidStr = UUIDObject.get("id").toString();
            if(uuidStr == null) return null;
            if(uuidStr.length() != 32) return null;
            byte[] bytes = Hex.decode(uuidStr.toUpperCase());
            return UuidEx.fromBytes(bytes);
        } catch (IOException | ParseException e) {
            return null;
        }
    }

    @Nullable public static String resolveName(UUID uuid) {
        try {
            String hexUuid = Hex.encode(UuidEx.of(uuid).toBytes()).toLowerCase();
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + hexUuid);
            StringBuilder sb = new StringBuilder();
            try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                    sb.append("\n");
                }
            }
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(sb.toString());
            return UUIDObject.get("name").toString();
        } catch (IOException | ParseException e) {
            return null;
        }
    }

}
