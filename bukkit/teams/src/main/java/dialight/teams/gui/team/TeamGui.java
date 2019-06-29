package dialight.teams.gui.team;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamGui extends Gui {

    private final Teams proj;
    private final ObservableTeam oteam;
    private final Map<UUID, TeamLayout> layoutMap = new HashMap<>();

    public TeamGui(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        this.oteam = oteam;
    }

    private TeamLayout getOrCreateLayout(Player player) {
        TeamLayout state = layoutMap.get(player.getUniqueId());
        if(state != null) return state;
        state = new TeamLayout(proj, oteam);
        layoutMap.put(player.getUniqueId(), state);
        return state;
    }

    @Override
    public View createView(Player player) {
        return new TeamView(this, getOrCreateLayout(player));
    }

    public ObservableTeam getOteam() {
        return oteam;
    }

    public Teams getProj() {
        return proj;
    }

}
