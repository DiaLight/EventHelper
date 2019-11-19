package dialight.teleporter;

import dialight.eventhelper.project.ProjectApi;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TeleporterApi implements ProjectApi {

    private final Teleporter proj;

    public TeleporterApi(Teleporter proj) {
        this.proj = proj;
    }

    @NotNull public SelectedPlayers getSelectedPlayers(UUID uuid) {
        return proj.getSelectedPlayers(uuid);
    }

}
