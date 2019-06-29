package dialight.offlinelib;

import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.OfflinePlayerEx;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
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
    @Nullable public OfflinePlayerEx getOfflinePlayerEx(UUID uuid) {
        return proj.getOrLoad(uuid);
    }

    @NotNull public UuidPlayer getUuidPlayer(UUID uuid) {
        return new UuidPlayer(proj, uuid);
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

}
