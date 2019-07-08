package dialight.offlinelib;

import dialight.extensions.OfflinePlayerEx;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
//        OfflinePlayerEx opex = proj.getOrLoad(uuid);
//        if(opex != null) {
//            name = opex.getName();
//            if(name != null) return name;
//        }
        return uuid.toString();
    }

    @Nullable public Player getPlayer() {
        return proj.getPlugin().getServer().getPlayer(uuid);
    }

    @Nullable public OfflinePlayer getOfflinePlayer() {
        OfflinePlayerEx opex = proj.getOrLoad(uuid);
        if(opex == null) return null;
        return proj.getPlugin().getServer().getOfflinePlayer(uuid);
    }

    public boolean isOnline() {
        Player player = getPlayer();
        return player != null;
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
