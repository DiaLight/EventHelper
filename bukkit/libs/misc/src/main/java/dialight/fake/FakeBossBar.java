package dialight.fake;

import dialight.extensions.ServerEx;
import dialight.nms.PlayerNms;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class FakeBossBar implements Listener {

    private final Map<UUID, PlayerState> bars = new LinkedHashMap<>();
    private final Set<UUID> offline = new HashSet<>();
    private final JavaPlugin plugin;
    private boolean enabled = false;

    private String text = null;
    private float healthPercent = 0f;
    private BossBarEntityFk entity;

    public FakeBossBar(JavaPlugin plugin) {
        this.plugin = plugin;
        entity = BossBarEntityFk.create(ServerEx.of(plugin.getServer()).getMainWorld());
    }

    private void enable() {
        if(enabled) return;
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(this, plugin);
//        plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, 20, 20);
        enabled = true;
    }

//    private void tick() {
//        for (PlayerState ps : bars.values()) entity.sendTeleport(ps.pbc);
//    }

    private void disable() {
        if(!enabled) return;
        HandlerList.unregisterAll(this);
        enabled = false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(!offline.contains(player.getUniqueId())) return;
        add(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerState ps = bars.remove(player.getUniqueId());
        if(ps == null) return;
        entity.sendDespawn(ps.pnms);
        offline.add(player.getUniqueId());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent e) {
        PlayerState ps = bars.get(e.getPlayer().getUniqueId());
        if(ps == null) return;
        float yaw = e.getTo().getYaw();
        float pitch = e.getTo().getPitch() + 90;
        if(Math.abs(yaw - ps.yaw) > 30f || Math.abs(pitch - ps.pitch) > 30f) {
            Location loc = e.getTo();
            Vector vector = loc.getDirection().normalize().multiply(23);
            entity.sendTeleport(ps.pnms, loc.clone().add(vector));
            ps.yaw = yaw;
            ps.pitch = pitch;
        }
    }

    public void add(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if(player != null) {
            add(player);
            return;
        }
        offline.add(uuid);
        if(!enabled) enable();
    }
    public void add(Player player) {
        PlayerState ps = new PlayerState(player);
        bars.put(player.getUniqueId(), ps);
        if(text != null) {
            spawnFor(ps, text, healthPercent);
        }
        if(!enabled) enable();
    }
    public void remove(UUID uuid) {
        offline.remove(uuid);
        PlayerState ps = bars.remove(uuid);
        if(ps == null) return;
        entity.sendDespawn(ps.pnms);
        if(bars.isEmpty()) disable();
    }

    public void clear() {
        despawn();
        bars.clear();
        disable();
    }

    private void spawnFor(PlayerState ps, String text, float healthPercent) {
        Location loc = ps.pnms.getPlayer().getLocation();
        loc = loc.clone().add(loc.getDirection().normalize().multiply(23));
        entity.sendSpawn(ps.pnms, loc, text, healthPercent);
    }
    public void spawn(String text, float healthPercent) {
        this.text = text;
        this.healthPercent = healthPercent;
        for (PlayerState ps : bars.values()) spawnFor(ps, text, healthPercent);
    }
    public void despawn() {
        for (PlayerState ps : bars.values()) entity.sendDespawn(ps.pnms);
        text = null;
    }
    public void updateText(String text) {
        this.text = text;
        for (PlayerState ps : bars.values()) entity.sendUpdateText(ps.pnms, text);
    }
    public void updateHealth(float healthPercent) {
        this.healthPercent = healthPercent;
        for (PlayerState ps : bars.values()) entity.sendUpdateHealth(ps.pnms, healthPercent);
    }
    public void updateBar(String text, float healthPercent) {
        this.text = text;
        this.healthPercent = healthPercent;
        for (PlayerState ps : bars.values()) entity.sendUpdateBar(ps.pnms, text, healthPercent);
    }

    private static class PlayerState {
        public final PlayerNms pnms;
        public float yaw;
        public float pitch;

        public PlayerState(Player player) {
            this.pnms = PlayerNms.of(player);
            this.yaw = player.getLocation().getYaw();
            this.pitch = player.getLocation().getPitch() + 90;
        }
    }

}
