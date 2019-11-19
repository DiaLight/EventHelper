package dialight.teams.gui.teams;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import dialight.teams.gui.addteam.AddTeamGui;
import dialight.teams.gui.team.TeamGui;
import dialight.teams.observable.ObservableScoreboard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TeamsGui extends Gui {

    @NotNull private final Teams proj;
    private final ObservableScoreboard scoreboard;
    private final AddTeamGui addTeamGui;
    private final TeamsView view;
    private final TeamsElement layout;
    private final Map<String, TeamGui> teamGuiMap = new HashMap<>();

    public TeamsGui(Teams proj, ObservableScoreboard scoreboard) {
        this.proj = proj;
        this.scoreboard = scoreboard;
        this.addTeamGui = new AddTeamGui(proj);
        this.layout = new TeamsElement(proj, scoreboard);
        this.view = new TeamsView(this, layout, scoreboard);

        scoreboard.teamsByName().onPut(this, (name, team) -> {
            teamGuiMap.put(name, new TeamGui(this.proj, team));
        });
        scoreboard.teamsByName().onRemove(this, (name, ot) -> {
            TeamGui teamGui = teamGuiMap.remove(ot.getName());
            for (Player viewer : teamGui.getViewers()) {
                viewer.closeInventory();
            }
        });
        scoreboard.teamsByName().forEach((name, ot) -> teamGuiMap.put(ot.getName(), new TeamGui(this.proj, ot)));
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
