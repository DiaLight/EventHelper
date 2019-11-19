package dialight.compatibility;

import dialight.nms.ReflectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;

public abstract class TeamBc {

    private static final Constructor<? extends TeamBc> constructor =
            ReflectionUtils.findCompatibleClass(TeamBc.class, Team.class);

    protected final Team team;

    public TeamBc(Team team) {
        this.team = team;
    }

    public abstract ChatColor getColor();
    public abstract void setColor(ChatColor color);

    public static TeamBc of(Team team) {
        return ReflectionUtils.newInstance(constructor, team);
    }

}
