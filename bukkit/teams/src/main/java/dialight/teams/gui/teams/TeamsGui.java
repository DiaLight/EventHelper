package dialight.teams.gui.teams;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import dialight.teams.gui.addteam.AddTeamGui;
import dialight.teams.gui.team.TeamGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TeamsGui extends Gui {

    @NotNull private final Teams proj;
    private final AddTeamGui addTeamGui;
    private final TeamsView view;
    private final TeamsLayout layout;
    private final Map<String, TeamGui> teamGuiMap = new HashMap<>();

    public TeamsGui(Teams proj) {
        this.proj = proj;
        this.addTeamGui = new AddTeamGui(proj);
        this.layout = new TeamsLayout(proj);
        this.view = new TeamsView(this, layout);

        ObservableCollection<? extends ObservableTeam> teams = proj.getTeamsInternal();

        teams.onAdd(ot -> {
            teamGuiMap.put(ot.getName(), new TeamGui(this.proj, ot));
        });
        teams.onRemove(ot -> {
            TeamGui teamGui = teamGuiMap.remove(ot.getName());
            for (Player viewer : teamGui.getViewers()) {
                viewer.closeInventory();
            }
        });
        teams.forEach(ot -> teamGuiMap.put(ot.getName(), new TeamGui(this.proj, ot)));
    }

    @Override public View createView(Player player) {
        return view;
    }

    @Nullable public Gui teamGui(String teamName) {
        return teamGuiMap.get(teamName);
    }

    @NotNull public Teams getProj() {
        return proj;
    }

    public AddTeamGui getAddTeamGui() {
        return addTeamGui;
    }

}
