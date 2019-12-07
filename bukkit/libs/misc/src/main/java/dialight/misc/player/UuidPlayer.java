package dialight.misc.player;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.LocationEx;
import dialight.nms.ItemStackNms;
import dialight.nms.NbtTagCompoundNms;
import dialight.nms.NbtTagListNms;
import dialight.nms.PlayerInventoryNms;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UuidPlayer {

    private final Server server;
    private final UUID uuid;
    private final NbtPlayer nbtp;
    private String cachedName = null;
    private Boolean pirateCache = null;

    public UuidPlayer(Server server, UUID uuid, String name, NbtPlayer nbtp) {
        this.server = server;
        this.uuid = uuid;
        this.cachedName = name;
        this.nbtp = nbtp;

        if(cachedName != null) {
            pirateCache = PlayerEngine.generateOfflineUuid(cachedName).equals(uuid);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isPirate() {
        if(pirateCache != null) return pirateCache;
        if (isOnline()) {
            pirateCache = !server.getOnlineMode();
            return pirateCache;
        }
        pirateCache = server.getOnlineMode();
        return pirateCache;
    }

    public boolean isAlien() {
        return isPirate() ^ !server.getOnlineMode();
    }

    @NotNull public String getName() {
        if(cachedName != null) return cachedName;
        return "null";
    }

    @NotNull public OfflinePlayer getOfflinePlayer() {
        return server.getOfflinePlayer(uuid);
    }
    @Nullable public Player getPlayer() {
        return server.getPlayer(uuid);
    }

    public boolean isOnline() {
        Player player = getPlayer();
        return player != null;
    }

    public boolean isOffline() {
        return !isOnline();
    }

    @NotNull public Location getLocation() {
        Player player = getPlayer();
        if(player != null) return player.getLocation();
        return nbtp.getLocation();
    }
    @NotNull public Location getHeadLocation() {
        Player player = getPlayer();
        if(player != null) return player.getLocation().clone().add(0, player.getEyeHeight(), 0);
        return nbtp.getLocation().clone().add(0, 1.62D, 0);
    }

    public void setLocation(Location loc) {
        Player player = getPlayer();
        if(player != null) {
            player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return;
        }
        nbtp.setLocation(loc);
    }
    @NotNull public Vector getVelocity() {
        Player player = getPlayer();
        if(player != null) return player.getVelocity();
        return nbtp.getVelocity();
    }
    public void setVelocity(Vector vec) {
        Player player = getPlayer();
        if(player != null) {
            player.setVelocity(vec);
            return;
        }
        this.nbtp.setVelocity(vec);
    }


    public boolean isFlying() {
        Player player = getPlayer();
        if(player != null) return player.isFlying();
        return nbtp.isFlying();
    }
    public void setFlying(boolean value) {
        Player player = getPlayer();
        if(player != null) {
            player.setFlying(value);
            return;
        }
        this.nbtp.setFlying(value);
    }

    public float getFlySpeed() {
        Player player = getPlayer();
        if(player != null) return player.getFlySpeed();
        return this.nbtp.getFlySpeed();
    }
    public void setFlySpeed(float value) {
        Player player = getPlayer();
        if(player != null) {
            player.setFlySpeed(value);
            return;
        }
        nbtp.setFlySpeed(value);
    }

    public boolean getAllowFlight() {
        Player player = getPlayer();
        if(player != null) return player.getAllowFlight();
        return nbtp.getAllowFlight();
    }
    public void setAllowFlight(boolean value) {
        Player player = getPlayer();
        if(player != null) {
            player.setAllowFlight(value);
            return;
        }
        nbtp.setAllowFlight(value);
    }

    public GameMode getGameMode() {
        Player player = getPlayer();
        if(player != null) return player.getGameMode();
        return nbtp.getGameMode();
    }
    public void setGameMode(GameMode value) {
        Player player = getPlayer();
        if(player != null) {
            player.setGameMode(value);
            return;
        }
        nbtp.setGameMode(value);
    }

    public void teleport(Location loc) {
        loc = LocationEx.of(loc).findSafeLoc();
        Location upLoc = this.getLocation();
        loc = LocationEx.of(upLoc).keepRotation(loc);
        this.setLocation(loc);
    }
    public void clearInventory() {
        Player player = getPlayer();
        if(player != null) {
            player.getInventory().clear();
            return;
        }
        nbtp.setInventory(NbtTagListNms.create());
    }
    public void setItemInMainHand(ItemStack item) {
        Player player = getPlayer();
        if(player != null) {
            PlayerInventoryBc.of(player.getInventory()).setItemInMainHand(item);
            return;
        }
        nbtp.setItemInMainHand(ItemStackNms.of(item).serialize());
    }
    public void setItemInMainHand(NbtTagCompoundNms itemNbt) {
        Player player = getPlayer();
        if(player != null) {
            PlayerInventoryBc.of(player.getInventory()).setItemInMainHand(ItemStackNms.fromNbt(itemNbt).asBukkit());
            return;
        }
        nbtp.setItemInMainHand(itemNbt);
    }
    public void setItemInInventory(int index, ItemStack item) {
        Player player = getPlayer();
        if(player != null) {
            player.getInventory().setItem(index, item);
            return;
        }
        nbtp.setItemInInventory(index, ItemStackNms.of(item).serialize());
    }
    public void setItemInInventory(int index, NbtTagCompoundNms itemNbt) {
        Player player = getPlayer();
        if(player != null) {
            player.getInventory().setItem(index, ItemStackNms.fromNbt(itemNbt).asBukkit());
            return;
        }
        nbtp.setItemInInventory(index, itemNbt);
    }

    public NbtTagListNms getNbtInventory() {
        Player player = getPlayer();
        if(player != null) {
            return PlayerInventoryNms.of(player.getInventory()).getNbtContent();
        }
        return nbtp.getInventory();
    }
    public void setNbtInventory(NbtTagListNms inv) {
        Player player = getPlayer();
        if(player != null) {
            PlayerInventoryNms.of(player.getInventory()).setNbtContent(inv);
            return;
        }
        nbtp.setInventory(inv);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UuidPlayer that = (UuidPlayer) o;
        return uuid.equals(that.uuid);
    }

    @Override public int hashCode() {
        return uuid.hashCode();
    }

}
