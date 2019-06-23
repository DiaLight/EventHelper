package dialight.teams;

import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import org.jetbrains.annotations.Nullable;

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

    public void addControlItem(Slot slot) {
        proj.getControlGui().addControlItem(slot);
    }
}
