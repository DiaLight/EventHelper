package dialight.offlinelib;

import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.OfflinePlayerEx;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class OfflineLibApi implements ProjectApi {

    private final OfflineLib proj;

    public OfflineLibApi(OfflineLib proj) {
        this.proj = proj;
    }

    @Nullable public OfflinePlayer getOfflinePlayerByName(String name) {
        return proj.getOfflinePlayerByName(name);
    }
    @Nullable public OfflinePlayerEx getOfflinePlayerEx(UUID uuid) {
        return proj.getOrLoad(uuid);
    }

    @NotNull public UuidPlayer getUuidPlayer(UUID uuid) {
        UuidPlayer up = proj.getNotPlayer(uuid);
        if(up != null) return up;
        return new UuidPlayer(proj, uuid);
    }
    @NotNull public UuidPlayer getUuidPlayer(Player player) {
        return new UuidPlayer(proj, player.getUniqueId());
    }
    @NotNull public UuidPlayer createUuidNotPlayer(String member) {
        return proj.getOrCreateNotPlayer(member);
    }

    @Nullable public OfflinePlayerEx getOfflinePlayerExByEntity(Entity entity) {
        return proj.getOfflinePlayerExByEntity(entity);
    }

    public OnlineObservable getOnline() {
        return proj.getOnline();
    }

    public OfflineObservable getOffline() {
        return proj.getOffline();
    }

    public Collection<UuidPlayer> getNotPlayers() {
        return proj.getNotPlayers();
    }
}
