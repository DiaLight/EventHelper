package dialight.teams;

import dialight.misc.ColorConverter;
import dialight.misc.player.UuidPlayer;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class TeamSortResult {

    private final String name;
    private final ChatColor color;
    protected final List<UuidPlayer> members = new ArrayList<>();

    public TeamSortResult(ObservableTeam team) {
        this.name = team.getName();
        this.color = team.color().getValue();
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public List<UuidPlayer> getMembers() {
        return members;
    }

    public Color getLeatherColor() {
        return ColorConverter.toLeatherColor(color);
    }

    public void addMember(UuidPlayer uuidPlayer) {
        members.add(uuidPlayer);
    }

}
