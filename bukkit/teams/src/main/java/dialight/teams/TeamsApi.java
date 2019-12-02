package dialight.teams;

import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.slot.Slot;
import dialight.misc.player.UuidPlayer;
import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.teams.observable.ObservableScoreboardManager;
import dialight.teams.observable.ObservableTeam;

import java.util.List;
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

    public List<UuidPlayer> collectSortMembers() {
        return proj.collectSortMembers();
    }

    public List<ObservableTeam> collectSortTeams() {
        return proj.collectSortTeams();
    }

    public ObservableObject<Map<String, ? extends TeamSortResult>> getSortResult() {
        return proj.getSortResult();
    }

}
