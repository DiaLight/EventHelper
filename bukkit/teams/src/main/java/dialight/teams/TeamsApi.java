package dialight.teams;

import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.slot.Slot;
import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.teams.observable.ObservableScoreboardManager;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

public class TeamsApi implements ProjectApi {

    private final Teams proj;

    public TeamsApi(Teams proj) {
        this.proj = proj;
    }

    public ObservableScoreboardManager getScoreboardManager() {
        return proj.getScoreboardManager();
    }

    public void addControlItem(Slot slot) {
        proj.getSortGui().addControlItem(slot);
    }

    public ObservableSet<String> getTeamWhiteList() {
        return proj.getTeamWhiteList();
    }

    public ObservableSet<UUID> getPlayerBlackList() {
        return proj.getPlayerBlackList();
    }

    public ObservableObject<Boolean> getOfflineMode() {
        return proj.getOfflineMode();
    }
    public boolean isOfflineMode() {
        return proj.isOfflineMode();
    }
    public void setOfflineMode(boolean offlineMode) {
        proj.setOfflineMode(offlineMode);
    }

    public Map<String, Location> getTeamEntryPoints() {
        return proj.getTeamEntryPoints();
    }

}
