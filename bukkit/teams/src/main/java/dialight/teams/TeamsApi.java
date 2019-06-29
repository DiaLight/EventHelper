package dialight.teams;

import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.Viewable;
import dialight.guilib.slot.Slot;
import dialight.observable.ObservableObject;
import dialight.observable.collection.ObservableCollection;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiConsumer;

public class TeamsApi implements ProjectApi {

    private final Teams proj;

    public TeamsApi(Teams proj) {
        this.proj = proj;
    }

    public ObservableCollection<ObservableTeam> getTeams() {
        return this.proj.getTeamsImmutable();
    }

    @Nullable public ObservableTeam get(String teamName) {
        return proj.get(teamName);
    }

    public void onMemberJoin(BiConsumer<ObservableTeam, String> op) {
        proj.onMemberJoin(op);
    }
    public void unregisterOnMemberJoin(BiConsumer<ObservableTeam, String> op) {
        proj.unregisterOnMemberJoin(op);
    }
    public void onMemberLeave(BiConsumer<ObservableTeam, String> op) {
        proj.onMemberLeave(op);
    }
    public void unregisterOnMemberLeave(BiConsumer<ObservableTeam, String> op) {
        proj.unregisterOnMemberLeave(op);
    }


    public void addControlItem(Slot slot) {
        proj.getControlGui().addControlItem(slot);
    }

    public void registerTeamsObserver(Viewable viewable) {
        proj.getListener().registerTeamsObserver(new ViewableTeamObserver(viewable));
    }

    public void unregisterTeamsObserver(Viewable viewable) {
        proj.getListener().unregisterTeamsObserver(new ViewableTeamObserver(viewable));
    }

    public ObservableCollection<String> getTeamFilter() {
        return proj.getTeamFilter();
    }

    public ObservableCollection<UUID> getPlayerFilter() {
        return proj.getPlayerFilter();
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

}
