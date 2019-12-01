package dialight.teams.captain;

import dialight.misc.ColorConverter;
import dialight.misc.player.UuidPlayer;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.List;

public class TeamSortResult {

    private final String name;
    private final ChatColor color;
    private final UuidPlayer captain;
    private final List<UuidPlayer> members;

    public TeamSortResult(ObservableTeam team, UuidPlayer captain, List<UuidPlayer> members) {
        this.name = team.getName();
        this.color = team.color().getValue();
        this.captain = captain;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public UuidPlayer getCaptain() {
        return captain;
    }

    public List<UuidPlayer> getMembers() {
        return members;
    }

    public Color getLeatherColor() {
        return ColorConverter.toLeatherColor(color);
    }

}
