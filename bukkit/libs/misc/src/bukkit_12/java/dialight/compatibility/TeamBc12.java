package dialight.compatibility;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class TeamBc12 extends TeamBc {

    public TeamBc12(Team team) {
        super(team);
    }

    @Override
    public ChatColor getColor() {
        return team.getColor();
    }

    @Override
    public void setColor(ChatColor color) {
        team.setColor(color);
        team.setPrefix(color.toString());
    }

}
