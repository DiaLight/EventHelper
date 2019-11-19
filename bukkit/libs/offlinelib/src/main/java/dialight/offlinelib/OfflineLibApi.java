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

    @NotNull public UuidPlayer getUuidPlayer(UUID uuid) {
        return proj.getUuidPlayer(uuid);
    }
    @NotNull public UuidPlayer getUuidPlayer(Player player) {
        return proj.getUuidPlayer(player.getUniqueId());
    }
    @NotNull public UuidPlayer getOrCreateNotPlayer(String member) {
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
