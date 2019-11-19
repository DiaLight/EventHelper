package dialight.offlinelib;

import dialight.extensions.LocationEx;
import dialight.extensions.OfflinePlayerEx;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UuidPlayer {

    private final OfflineLib proj;
    private final UUID uuid;

    public UuidPlayer(OfflineLib proj, UUID uuid) {
        this.proj = proj;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        OfflinePlayer op = proj.getPlugin().getServer().getOfflinePlayer(uuid);
        String name = op.getName();
        if(name != null) return name;
        return uuid.toString();
    }

    @Nullable public Player getPlayer() {
        return proj.getPlugin().getServer().getPlayer(uuid);
    }

    @Nullable public OfflinePlayerEx getOfflinePlayer() {
        return proj.getOrLoad(uuid);
    }

    public boolean inPlayerdata() {
        return getOfflinePlayer() != null;
    }

    public boolean isOnline() {
        Player player = getPlayer();
        return player != null;
    }
    public boolean isOffline() {
        return !isOnline();
    }

    @Nullable public Location getLocation() {
        Player player = getPlayer();
        if(player != null) return player.getLocation();
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) return opex.getLocation();
        return null;
    }

    public boolean setLocation(Location loc) {
        Player player = getPlayer();
        if(player != null) {
            player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) {
            opex.setLocation(loc);
            return true;
        }
        return false;
    }
    @NotNull public Vector getVelocity() {
        Player player = getPlayer();
        if(player != null) return player.getVelocity();
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) return opex.getVelocity();
        return null;
    }
    public boolean setVelocity(Vector vec) {
        Player player = getPlayer();
        if(player != null) {
            player.setVelocity(vec);
            return false;
        }
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) {
            opex.setVelocity(vec);
            return true;
        }
        return false;
    }


    public boolean isFlying() {
        Player player = getPlayer();
        if(player != null) return player.isFlying();
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) return opex.isFlying();
        return false;
    }
    public boolean setFlying(boolean value) {
        Player player = getPlayer();
        if(player != null) {
            player.setFlying(value);
            return true;
        }
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) {
            opex.setFlying(value);
            return true;
        }
        return false;
    }

    public float getFlySpeed() {
        Player player = getPlayer();
        if(player != null) return player.getFlySpeed();
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) return opex.getFlySpeed();
        return 0.0f;
    }
    public boolean setFlySpeed(float value) {
        Player player = getPlayer();
        if(player != null) {
            player.setFlySpeed(value);
            return true;
        }
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) {
            opex.setFlySpeed(value);
            return true;
        }
        return false;
    }

    public boolean getAllowFlight() {
        Player player = getPlayer();
        if(player != null) return player.getAllowFlight();
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) return opex.getAllowFlight();
        return false;
    }
    public boolean setAllowFlight(boolean value) {
        Player player = getPlayer();
        if(player != null) {
            player.setAllowFlight(value);
            return true;
        }
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) {
            opex.setAllowFlight(value);
            return true;
        }
        return false;
    }

    public GameMode getGameMode() {
        Player player = getPlayer();
        if(player != null) return player.getGameMode();
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) return opex.getGameMode();
        return null;
    }
    public boolean setGameMode(GameMode value) {
        Player player = getPlayer();
        if(player != null) {
            player.setGameMode(value);
            return true;
        }
        OfflinePlayerEx opex = getOfflinePlayer();
        if(opex != null) {
            opex.setGameMode(value);
            return true;
        }
        return false;
    }

    public boolean teleport(Location loc) {
        loc = LocationEx.of(loc).findSafeLoc();
        Location upLoc = this.getLocation();
        if(upLoc != null) {
            loc = LocationEx.of(upLoc).keepRotation(loc);
        }
        return this.setLocation(loc);
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
