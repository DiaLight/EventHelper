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

    @NotNull public OfflinePlayer getOfflinePlayerByName(String name) {
        return proj.getOfflinePlayerByName(name);
    }
    @NotNull public OfflinePlayerEx getOfflinePlayerEx(UUID uuid) {
        return proj.getOrCreate(uuid);
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
