package dialight.offlinelib;

import dialight.eventhelper.project.ProjectApi;
import dialight.misc.player.PlayerEngine;
import dialight.misc.player.UuidPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OfflineLibApi implements ProjectApi {

    private final OfflineLib proj;

    public OfflineLibApi(OfflineLib proj) {
        this.proj = proj;
    }

    @Nullable public OfflinePlayer getOfflinePlayerByName(String name) {
        return proj.getOfflinePlayerByName(name);
    }

    @Deprecated
    @NotNull public UuidPlayer getUuidPlayer(UUID uuid) {
        return proj.getUuidPlayer(uuid);
    }
    @Deprecated
    @NotNull public UuidPlayer getUuidPlayer(Player player) {
        return proj.getUuidPlayer(player.getUniqueId());
    }
    @Deprecated
    @NotNull public UuidPlayer getUuidPlayer(String name) {
        return proj.getUuidPlayer(name);
    }
    public PlayerEngine getPlayerEngine() {
        return proj.getPlayerEngine();
    }

    @Nullable public UuidPlayer getUuidPlayerExByEntity(Entity entity) {
        return proj.getUuidPlayerExByEntity(entity);
    }

    public OnlineObservable getOnline() {
        return proj.getOnline();
    }

    public OfflineObservable getOffline() {
        return proj.getOffline();
    }

}
